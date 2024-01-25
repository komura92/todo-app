package com.example.todoapp.infrastructure;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.example.todoapp.infrastructure.containers.PostgresContainer;
import com.example.todoapp.infrastructure.containers.TestKeycloakContainer;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class BaseApiTest {


    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected Environment environment;

    @BeforeAll
    public static void beforeAll() {
        TestKeycloakContainer.init();
        PostgresContainer.init();
    }
}
