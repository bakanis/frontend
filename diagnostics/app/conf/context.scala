package conf

import common._
import com.gu.management._
import com.gu.management.logback.LogbackLevelPage
import metrics._

object DiagnosticsConfiguration extends BaseGuardianConfiguration("frontend-diagnostics") {
  object nginx {
    lazy val log: String = configuration.getStringProperty("nginx.log") getOrElse {
      throw new IllegalStateException("NGINX log file property not configured")
    }
  }
}

object Management extends com.gu.management.play.Management {
  val applicationName = DiagnosticsConfiguration.application

  lazy val pages = List(
    new ManifestPage,
    new UrlPagesHealthcheckManagementPage("/"),
    StatusPage(applicationName, NginxLog.metrics),
    new PropertiesPage(DiagnosticsConfiguration.toString),
    new LogbackLevelPage(applicationName)
  )
}
