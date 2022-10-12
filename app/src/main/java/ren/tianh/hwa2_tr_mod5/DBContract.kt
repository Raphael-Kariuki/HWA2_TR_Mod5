package ren.tianh.hwa2_tr_mod5

import android.provider.BaseColumns

class DBContract {

    /*inner class that defines the table contents*/

    class UserEntry: BaseColumns{
        companion object{
            const val TABLE_NAME = "users"
            const val COLUMN_USER_ID = "userId"
            const val COLUMN_FULL_NAMES = "userFullNames"
            const val COLUMN_USERNAME = "userName"
            const val COLUMN_PASSWORD = "password"
        }
    }
}