package controllers

import common._
import model._
import play.api.mvc.{ Controller, Action }
import conf.FootballClient
import pa.FootballMatch
import play.api.libs.concurrent.Akka
import play.api.Play._
import org.joda.time.format.DateTimeFormat
import feed._
import org.joda.time._
import pa.LineUp
import scala.Some
import model.Team

case class MatchPage(theMatch: FootballMatch, lineUp: LineUp) extends MetaData {
  lazy val hasLiveMatch = theMatch.isLive || theMatch.isResult
  lazy val hasLineUp = lineUp.awayTeam.players.nonEmpty && lineUp.homeTeam.players.nonEmpty

  override lazy val canonicalUrl = None
  override lazy val id = MatchUrl(theMatch)
  override lazy val section = "football"
  override lazy val webTitle = "%s %s - %s %s ".format(theMatch.homeTeam.name, theMatch.homeTeam.score.getOrElse(""),
    theMatch.awayTeam.score.getOrElse(""), theMatch.awayTeam.name)

  override lazy val analyticsName = "GFE:Football:automatic:match:%s:%s v %s".format(
    theMatch.date.toString("dd MMM YYYY"), theMatch.homeTeam.name, theMatch.awayTeam.name)

}

object MatchController extends Controller with Logging {

  private val dateFormat = DateTimeFormat.forPattern("yyyyMMMdd")

  def renderMatchId(matchId: String) = render(Competitions.findMatch(matchId))

  def renderMatch(year: String, month: String, day: String, home: String, away: String) = {

    val date = dateFormat.parseDateTime(year + month + day).toDateMidnight

    (TeamMap.findTeamIdByUrlName(home), TeamMap.findTeamIdByUrlName(away)) match {
      case (Some(homeId), Some(awayId)) => render(Competitions.matchFor(date, homeId, awayId))
      case _ => render(None)
    }
  }

  private def render(maybeMatch: Option[FootballMatch]) = Action { implicit request =>
    maybeMatch.map { theMatch =>
      val promiseOfLineup = Akka.future(FootballClient.lineUp(theMatch.id))
      Async {
        promiseOfLineup.map { lineUp =>
          Cached(60) {
            Ok(Compressed(views.html.footballMatch(MatchPage(theMatch, lineUp))))
          }
        }
      }
    }.getOrElse(NotFound)
  }
}
