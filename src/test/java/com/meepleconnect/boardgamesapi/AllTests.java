package com.meepleconnect.boardgamesapi;

import com.meepleconnect.boardgamesapi.controllers.AnalyticsControllerIT;
import com.meepleconnect.boardgamesapi.controllers.BoardgameControllerIT;
import com.meepleconnect.boardgamesapi.controllers.EasterEggControllerIT;
import com.meepleconnect.boardgamesapi.controllers.ExceptionControllerIT;
import com.meepleconnect.boardgamesapi.controllers.FileControllerIT;
import com.meepleconnect.boardgamesapi.controllers.HealthControllerIT;
import com.meepleconnect.boardgamesapi.controllers.JwtAuthenticationControllerIT;
import com.meepleconnect.boardgamesapi.controllers.PublicControllerIT;
import com.meepleconnect.boardgamesapi.controllers.PublisherControllerIT;
import com.meepleconnect.boardgamesapi.controllers.ReservationControllerIT;
import com.meepleconnect.boardgamesapi.controllers.ReviewControllerIT;
import com.meepleconnect.boardgamesapi.controllers.SearchControllerIT;
import com.meepleconnect.boardgamesapi.controllers.SecureControllerIT;
import com.meepleconnect.boardgamesapi.controllers.StatisticsControllerIT;
import com.meepleconnect.boardgamesapi.controllers.UserControllerIT;
//import com.meepleconnect.boardgamesapi.services.BoardgameServiceTest;
//import com.meepleconnect.boardgamesapi.services.ReservationServiceTest;
//import com.meepleconnect.boardgamesapi.services.UserServiceTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        AnalyticsControllerIT.class,
        BoardgameControllerIT.class,
        EasterEggControllerIT.class,
        ExceptionControllerIT.class,
        FileControllerIT.class,
        HealthControllerIT.class,
        JwtAuthenticationControllerIT.class,
        PublicControllerIT.class,
        PublisherControllerIT.class,
        ReservationControllerIT.class,
        ReviewControllerIT.class,
        SearchControllerIT.class,
        SecureControllerIT.class,
        StatisticsControllerIT.class,
        UserControllerIT.class
//// BoardgameServiceTest.class,
//// ReservationServiceTest.class,
//// UserServiceTest.class
})
public class AllTests {
    // Deze klasse dient alleen als container voor de test suite
    // Als je deze runt, zie je alle tests in de geselecteerde klassen op 100% code coverage komen.
}