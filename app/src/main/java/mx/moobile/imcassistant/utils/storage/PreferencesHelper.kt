package mx.moobile.imcassistant.utils.storage

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * Object that manage the shared preferences
 *
 */
object PreferencesHelper {

    /**
     * Create a instance with default settings
     * @param[context] Context of application.
     */
    fun defaultPrefs(context: Context): SharedPreferences
            = PreferenceManager.getDefaultSharedPreferences(context)

    /**
     * Create a instance with custom settings
     * @param[context] Context of application.
     * @param[name] Name for instance of shared preference.
     */
    fun customPrefs(context: Context, name: String): SharedPreferences
            = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    /**
     * Puts a key value pair in shared prefs if doesn't exists, otherwise update value on given
     * @param[key] key unique for the value to save
     * @param[value] value to save
     */
    operator fun SharedPreferences.set(key: String, value: Any) {
        when (value) {
            is String -> edit{ it.putString(key, value) }
            is Int -> edit{ it.putInt(key, value) }
            is Boolean -> edit{ it.putBoolean(key, value) }
            is Float -> edit{ it.putFloat(key, value) }
            is Long -> edit{ it.putLong(key, value) }
            else -> throw UnsupportedOperationException("[PreferenceHelper.set] -> Not yet implemented")
        }
    }

    /**
     * Finds value on given key
     * [T] is the type of value.
     * @param[key] String key of value to find
     * @param[defaultValue] optional default value - will take null for strings, false for bool and
     * -1 for numeric values if [defaultValue] is not specified
     */
    inline operator fun <reified T : Any> SharedPreferences.get(key: String, defaultValue: T? = null): T? {
        return when (T::class) {
            String::class -> getString(key, defaultValue as? String) as T?
            Int::class -> getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> getLong(key, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException("[PreferenceHelper.get] -> Not yet implemented")
        }
    }

    /**
     * Extension function for apply operation to Shared Preferences
     * @param[operation] lambda of type SharedPreferences.Editor
     */
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        this.edit().run {
            operation(this)
            this.apply()
        }
    }
}