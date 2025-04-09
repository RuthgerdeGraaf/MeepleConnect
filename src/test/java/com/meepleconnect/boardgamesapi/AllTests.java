package com.meepleconnect.boardgamesapi;

import com.meepleconnect.boardgamesapi.controllers.BoardGameControllerIT;
import com.meepleconnect.boardgamesapi.controllers.EasterEggControllerTest;
import com.meepleconnect.boardgamesapi.controllers.ExceptionControllerTest;
import com.meepleconnect.boardgamesapi.controllers.JwtAuthenticationControllerTest;
import com.meepleconnect.boardgamesapi.controllers.PublisherControllerIT;
import com.meepleconnect.boardgamesapi.controllers.ReservationControllerIT;
import com.meepleconnect.boardgamesapi.controllers.UserControllerTest;
import com.meepleconnect.boardgamesapi.services.BoardgameServiceTest;
import com.meepleconnect.boardgamesapi.services.ReservationServiceTest;
import com.meepleconnect.boardgamesapi.services.UserServiceTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    BoardGameControllerIT.class,
    EasterEggControllerTest.class,
    ExceptionControllerTest.class,
    JwtAuthenticationControllerTest.class,
    PublisherControllerIT.class,
    ReservationControllerIT.class,
    UserControllerTest.class,
    BoardgameServiceTest.class,
    ReservationServiceTest.class,
    UserServiceTest.class
})
public class AllTests {
    // Deze klasse dient alleen als container voor de test suite
} 