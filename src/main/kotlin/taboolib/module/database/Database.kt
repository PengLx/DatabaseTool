package taboolib.module.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

object Database {

    fun createDataSource(host: Host<*>, hikariConfig: HikariConfig? = null): DataSource {
        return HikariDataSource(hikariConfig ?: createHikariConfig(host))
    }

    fun createDataSourceWithoutConfig(host: Host<*>): DataSource {
        val config = HikariConfig()
        config.jdbcUrl = host.connectionUrl
        if (host is HostSQL) {
            config.username = host.user
            config.password = host.password
        } else {
            error("Unsupported host: $host")
        }
        return HikariDataSource(config)
    }

    fun createHikariConfig(host: Host<*>): HikariConfig {
        val config = HikariConfig()
        config.jdbcUrl = host.connectionUrl
        when (host) {
            is HostSQL -> {
                config.driverClassName = "com.mysql.jdbc.Driver"
                config.username = host.user
                config.password = host.password
            }
            is HostSQLite -> {
                config.driverClassName = "org.sqlite.JDBC"
            }
            else -> {
                error("Unsupported host: $host")
            }
        }
        config.isAutoCommit = true
        config.minimumIdle = 1
        config.maximumPoolSize = 10
        config.validationTimeout = 5000
        config.connectionTimeout = 30000
        config.idleTimeout = 600000
        config.maxLifetime = 1800000
        /**
        if (settingsFile.contains("DefaultSettings.ConnectionTestQuery")) {
            config.connectionTestQuery = settingsFile.getString("DefaultSettings.ConnectionTestQuery")
        }
        if (settingsFile.contains("DefaultSettings.DataSourceProperty")) {
            settingsFile.getConfigurationSection("DefaultSettings.DataSourceProperty")?.getKeys(false)?.forEach { key ->
                config.addDataSourceProperty(key, settingsFile.getString("DefaultSettings.DataSourceProperty.$key"))
            }
        }
        **/
        return config
    }
}