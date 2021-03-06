package test

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ GivenWhenThen, FeatureSpec }
import scala.collection.JavaConversions._

class MatchFeatureTest extends FeatureSpec with GivenWhenThen with ShouldMatchers {

  feature("MatchPage") {

    scenario("Visit match page") {

      given("I visit a match page")

      HtmlUnit("/football/match/3518296") { browser =>
        import browser._

        then("I should see the home team score")
        $("h2").getTexts should contain("Derby 0")

        and("I should see the away team score")
        $("h2").getTexts should contain("Blackburn 1")

        and("I should see the home possession")
        findFirst("[data-stat=Possession]").findFirst(".home-num").getText should be("54%")

        and("I should see the away possession")
        findFirst("[data-stat=Possession]").findFirst(".away-num").getText should be("46%")

        and("I should see the home corners")
        findFirst("[data-stat=Corners]").findFirst(".home-num").getText should be("7")

        and("I should see the away corners")
        findFirst("[data-stat=Corners]").findFirst(".away-num").getText should be("5")

        and("I should see the home team lineup")
        findFirst(".team-list").getText should include("John Brayford")

        and("I should see the away team lineup")
        $(".team-list")(1).getText should include("Colin Kazim-Richards")
      }
    }
  }
}
