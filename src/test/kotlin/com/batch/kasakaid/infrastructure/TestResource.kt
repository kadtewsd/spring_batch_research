package com.batch.kasakaid.infrastructure

import com.batch.kasakaid.infrastructure.helper.FileLoadHelper
import org.dbunit.database.DatabaseConfig
import org.dbunit.database.DatabaseConnection
import org.dbunit.dataset.Column
import org.dbunit.dataset.csv.CsvDataSet
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory
import org.dbunit.operation.DatabaseOperation
import org.junit.rules.ExternalResource
import org.springframework.core.env.Environment
import org.springframework.jdbc.datasource.DataSourceUtils
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.File
import kotlin.reflect.jvm.jvmName


// このクラスを Component にすると、Resource を拾ってこないでエラーになるので、Autowired できない。
// そのため、@TestComponent にしておいて、Bean に登録する。
@Component
class TestResource(val fileLoadHelper: FileLoadHelper, val masterDataSource: TransactionAwareDataSourceProxy, val environment: Environment) : ExternalResource() {

    /**
     * CSVデータ格納ディレクトリ
     */
    private val CSV_DIRECTORY = "src/test/resources/test_data/"

    @Override
    override fun before() {
        println(this::class.jvmName + "before start.")
    }

    @Override
    override fun after() {
        println(this::class.jvmName + "after start")
    }

    fun initialized(): Boolean {
        val con = masterDataSource.connection
        val st = con.createStatement()
        val result = st.executeQuery("select * from INFORMATION_SCHEMA.tables")
        var ret = false
        while (result.next()) {
            if (result.getString("table_name") == "TB_MST_SHRINE") {
                ret = true
                break
            }
        }
        st.close()
        con.close()
        return ret
    }

    fun insertMasterData() {
        this.executeOperationBy(DatabaseOperation.INSERT, MASTER_DATA)
    }

    fun executeOperationBy(operation: DatabaseOperation, vararg testData: String) {
        val dbConn: DatabaseConnection = openDbConn()
        try {
            // DatabaseConnectionの作成
            for (data in testData) {
                // データセットの取得
                val dataSet = CsvDataSet(File(CSV_DIRECTORY + data))

                noAutoValueOnZero(dbConn, dataSet)
                // セットアップ実行
                operation.execute(dbConn, dataSet)
            }
        } finally {
            // DatabaseConnectionの破棄
            closeDbConn(dbConn)
        }
    }

    fun insertByNormalInsert(vararg testData: String) {
        this.executeOperationBy(DatabaseOperation.INSERT, *testData)
    }

    /**
     * テストデータ投入メソッド
     *
     * @param testData テストデータ
     * @throws Exception 例外
     */
    fun insertData(vararg testData: String) {
        this.executeOperationBy(DatabaseOperation.CLEAN_INSERT, *testData)
    }

    fun noAutoValueOnZero(dbConn: DatabaseConnection, dataSet: CsvDataSet) {
        if (!isPostgreSQL()) return
        // auto increment に対して 0 を insert することを許可します。
        val statement = "SET SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO'"
        for (tableName in dataSet.getTableNames()) {
            val metaData = dataSet.getTableMetaData(tableName)
            val column = metaData.columns.filter { "id" == it.columnName }
                    .first()
            if (column != null) {
                println("test resource restoreBillScheduleStatus $statement")
                executeStatement(dbConn, statement)
            }
        }
    }

    /**
     * テストデータ投入メソッド
     *
     * @param testData テストデータ
     * @throws Exception 例外
     */
    fun insertDataOnly(vararg testData: String) {
        val dbConn: DatabaseConnection = openDbConn()
        try {
            for (data in testData) {
                // データセットの取得
                val dataSet = CsvDataSet(File(CSV_DIRECTORY + data))
                // セットアップ実行
                DatabaseOperation.INSERT.execute(dbConn, dataSet)
            }
        } finally {
            // DatabaseConnectionの破棄
            closeDbConn(dbConn)
        }
    }

    /**
     * テストデータ更新メソッド
     *
     * @param testData テストデータ
     * @throws Exception 例外
     */
    fun updateData(vararg testData: String) {
        this.executeOperationBy(DatabaseOperation.UPDATE, *testData)
    }

    /**
     * テストデータ削除メソッド
     *
     * @param testData テストデータ
     * @throws Exception 例外
     */
    fun deleteData(vararg testData: String) {
        this.executeOperationBy(DatabaseOperation.DELETE, *testData)
    }

    fun deleteAllData(vararg testData: String) {
        this.executeOperationBy(DatabaseOperation.DELETE_ALL, *testData)
    }

    /**
     * テストデータ削除メソッド
     *
     * @throws Exception 例外
     */
    fun truncateTable() {
        truncateTable("test_data/truncate")
    }

    fun deleteMasterData() {
        this.truncateTable("test_data/master_data")
    }

    /**
     * ディレクトリに存在する CSV ファイルにマップするテーブルを truncate します。
     * この際、auto_increment されている列については、1 から連番を振り直します。
     * 連番を振り直すテーブルのファイルには、"id" という名前の列、または、"Id" という文字列が含まれている列名を指定します。
     * 連番を振り直したくないテーブルについては、上記以外の文字 (例 : a) を記載します。
     *
     * @param directory
     */
    private fun truncateTable(directory: String) {
        val dbConn = openDbConn()
        executeStatement(dbConn, truncateReference("FALSE"))
        try {
            // データセットの取得
            val resouce = fileLoadHelper.asReader("./${directory}/table-ordering.txt")
            do {
                val reader = BufferedReader(resouce)
                val line = reader.readLine() ?: break
                val schema = environment.getProperty("application.db.schema.name")
                val result = if (schema == null) line else "\"$schema\".\"$line\""
                executeStatement(dbConn, "truncate table $result;")

            } while (true)


        } finally {
            // DatabaseConnectionの破棄
            executeStatement(dbConn, truncateReference("TRUE"))
            closeDbConn(dbConn)
        }
    }

    /**
     * auto_increment を再設定します。
     * マスタの product と unit_price は ID の自動採番を元に戻す必要はないので、対象外になります。
     *
     * @param tableName
     * @param column
     * @return
     */
    private fun autoIncrementReset(tableName: String, column: Column): String {
        return if (isPostgreSQL()) "ALTER TABLE ${tableName} AUTO_INCREMENT = 1" else "ALTER TABLE $tableName ALTER COLUMN ${column.columnName} RESTART WITH 1"
    }

    private fun isPostgreSQL(): Boolean {
        return environment.getProperty("spring.datasource.url")!!.contains("postgresql")
    }

    private fun truncateReference(value: String): String {
        val h2Reference = "SET REFERENTIAL_INTEGRITY ${value}"
        val hsqlReference = "SET DATABASE REFERENTIAL INTEGRITY ${value}"
        return if (isPostgreSQL()) "set foreign_key_checks = $value" else h2Reference
    }

    fun openDbConn(): DatabaseConnection {
        val databaseConnection = DatabaseConnection(DataSourceUtils.getConnection(masterDataSource))
        val config = databaseConnection.getConfig()
        config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, PostgresqlDataTypeFactory())
        return databaseConnection
    }

    fun closeDbConn(dbConn: DatabaseConnection) {
        dbConn.close()
    }

    private fun executeStatement(dbConnection: DatabaseConnection, statement: String) {
        val st = dbConnection.connection.createStatement()
//        println("execute statement ${statement}")
        st.execute(statement)
    }

//    fun executeDDL(path: String) {
//        val sqlFile = SqlFile(File(path))
//        sqlFile.execute()
//    }
    companion object {
        private const val MASTER_DATA = "master_data"
    }
}