#!/bin/sh

export PGPASSWORD=kazuteru
echo execute developDBDump.sh
sqlFileName=./kazuteru.sql
databaseName=kasakaidDB
userName=kazuteru

rm -f ./$sqlFileName

echo dumping kazuteru tables... to $sqlFileName
\cp -f ./.pgpass $HOME/
chmod 0600 $HOME/.pgpass
hostName=127.0.0.1
pg_dump --schema-only --username=$userName -d $databaseName --no-password --host=$hostName > $sqlFileName

sed -i '' 's/kazuteru\.//' $sqlFileName
sed -i '' 's/GRANT SELECT.*//' $sqlFileName
sed -i '' 's/SELECT pg_catalog.*//' $sqlFileName
sed -i '' 's/SET.*//' $sqlFileName

sed -i '' 's/DEFAULT nextval\(.\)/AUTO_INCREMENT/' $sqlFileName
# sed -i '' 's/default .+//' $sqlFileName
# sed -i '' 's/integer DEFAULT nextval\(.*\).*/INT\(10\) DEFAULT 1 NOT NULL AUTO_INCREMENT,/' $sqlFileName
sed -i '' 's/ALTER.*//' $sqlFileName
sed -i '' 's/ADD CONSTRAINT.*//' $sqlFileName
sed -i '' 's/::text//g' $sqlFileName
sed -E -i '' 's/::.*(.)[\s]*/\1 /g' $sqlFileName
sed -E -i '' "s/('.*')[A-Za-z0-9]/\1/g" $sqlFileName
sed -i '' 's/CONSTRAINT.*//' $sqlFileName
sed -i '' 's/varying//' $sqlFileName
sed -i '' 's/varying//' $sqlFileName
sed -i '' 's/character/nvarchar/' $sqlFileName
sed -E -i '' "s/(CREATE INDEX .+ ON .+) USING btree (\(.+\))/\1\2/g" $sqlFileName
# sed -i '' 's/SERIAL NOT NULL/NOT NULL AUTO_INCREMENT/' $sqlFileName

echo "place kazuteru.sql to build directory"

\cp -f $sqlFileName ./src/test/resources/

echo "deleting original schema file"
rm -f $sqlFileName

echo 'extract master table names'
tableOrdering="table-ordering.txt"
testData=./src/test/resources/test_data
masterDir=$testData/master_data
psql -U $userName -d $databaseName -h $hostName  -t -c "select relname as TABLE_NAME from pg_stat_user_tables where relname like 'tb_mst%'" > ./$tableOrdering

echo 'copying and deleting master table names'
cp ./$tableOrdering $masterDir/$tableOrdering
rm -f ./$tableOrdering

echo 'extract transaction table names'
psql -U $userName -d $databaseName -h $hostName  -t -c "select relname as TABLE_NAME from pg_stat_user_tables where relname like 'tb_trn%' or relname like 'BATACH_%'" > ./$tableOrdering

echo 'copying and deleting transaction table names'
truncateDir=$testData/truncate
\cp -f ./$tableOrdering $truncateDir/$tableOrdering
rm -f ./$tableOrdering
