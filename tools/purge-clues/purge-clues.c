#include <stdio.h>
#include <stdlib.h>
#include <sqlite3.h>

#define DATABASE_PATH "/aios/context/clues.sqlite3"
#define DELETE_STATEMENT "delete from clues;"

int main(int argc, char * argv[])
{
    sqlite3 * db;
    int rc;
    char *zErrMsg = 0;
    
    rc = sqlite3_open(DATABASE_PATH, &db);
    if (rc)
    {
      fprintf(stderr, "Can't open database: %s\n", sqlite3_errmsg(db));
      sqlite3_close(db);
      exit(1);
    }

    rc = sqlite3_exec(db, DELETE_STATEMENT, NULL, 0, &zErrMsg);
    if (rc != SQLITE_OK)
    {
      fprintf(stderr, "SQL error: %s\n", zErrMsg);
      sqlite3_close(db);
      exit(1);
    }
    
    sqlite3_close(db);
    printf("Clues deleted successfully!\n");
    return 0;
}