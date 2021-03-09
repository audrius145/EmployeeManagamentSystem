package model;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.*;

import java.util.concurrent.Semaphore;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class _test_DataHandler_employee
{
  private DataHandler data;

  @BeforeAll
  void init()
  {
    new JFXPanel();
  }

  @BeforeEach
  void setUp()
  {
    System.out.println("---> set up");
    data = new DataHandler();
  }

  @AfterEach
  void tearDown()
  {
    System.out.println("---> tear down");
  }

  @Test
  @Order(0)
  void Zero()
  {
    assertThrows(RuntimeException.class, () -> data.getEmployee(0));
    assertThrows(RuntimeException.class, () -> data.getEmployee(1));
    assertThrows(RuntimeException.class, () -> data.getEmployee(2));
  }

  @Test
  @Order(1)
  void One_Add()
  {
    data.employee(1,"employee1",1,true);
  }

  @Test
  @Order(2)
  void One_Get_plusBoundary()
  {
    assertThrows(RuntimeException.class, () -> data.getEmployee(1));
    data.employee(1,"employee1",1,true);
    waitForRunLater();
    assertEquals(1, data.getEmployee(1).getID());
    assertEquals("employee1", data.getEmployee(1).getName());
    assertEquals(1, data.getEmployee(1).getDepartment());
    assertTrue(data.getEmployee(1).isAdmin());
    assertThrows(RuntimeException.class, () -> data.getEmployee(0));
    assertThrows(RuntimeException.class, () -> data.getEmployee(2));
  }

  @Test
  @Order(3)
  void One_OverWrite_plusBoundary()
  {
    assertThrows(RuntimeException.class, () -> data.getEmployee(1));
    data.employee(1,"employee1",1,true);
    waitForRunLater();
    assertEquals(1, data.getEmployee(1).getID());
    assertEquals("employee1", data.getEmployee(1).getName());
    assertEquals(1, data.getEmployee(1).getDepartment());
    assertTrue(data.getEmployee(1).isAdmin());
    data.employee(1,"employee2",2,false);
    waitForRunLater();
    assertEquals(1, data.getEmployee(1).getID());
    assertEquals("employee2", data.getEmployee(1).getName());
    assertEquals(2, data.getEmployee(1).getDepartment());
    assertFalse(data.getEmployee(1).isAdmin());
    assertThrows(RuntimeException.class, () -> data.getEmployee(0));
    assertThrows(RuntimeException.class, () -> data.getEmployee(2));
  }

  @Test
  @Order(4)
  void One_Remove_plusBoundary()
  {
    assertThrows(RuntimeException.class, () -> data.getEmployee(1));
    data.employee(1,"employee1",1,true);
    waitForRunLater();
    data.employee(2,"employee2",2,false);
    waitForRunLater();
    assertEquals(1, data.getEmployee(1).getID());
    assertEquals("employee1", data.getEmployee(1).getName());
    assertEquals(1, data.getEmployee(1).getDepartment());
    assertTrue(data.getEmployee(1).isAdmin());
    assertEquals(2, data.getEmployee(2).getID());
    assertEquals("employee2", data.getEmployee(2).getName());
    assertEquals(2, data.getEmployee(2).getDepartment());
    assertFalse(data.getEmployee(2).isAdmin());
    assertThrows(RuntimeException.class, () -> data.getEmployee(0));
    assertThrows(RuntimeException.class, () -> data.getEmployee(3));
    data.removeEmployee(2);
    waitForRunLater();
    assertEquals(1, data.getEmployee(1).getID());
    assertEquals("employee1", data.getEmployee(1).getName());
    assertEquals(1, data.getEmployee(1).getDepartment());
    assertTrue(data.getEmployee(1).isAdmin());
    assertThrows(RuntimeException.class, () -> data.getEmployee(2));
    assertThrows(RuntimeException.class, () -> data.getEmployee(0));
    assertThrows(RuntimeException.class, () -> data.getEmployee(3));
  }

  @Test
  @Order(5)
  void Many_Get_plusBoundary()
  {
    assertThrows(RuntimeException.class, () -> data.getEmployee(1));
    for (int i = 1; i < 50; i++)
    {
      data.employee(i, "employee" + i, i, true);
      waitForRunLater();
    }
    for (int i = 1; i < 50; i++)
    {
      assertEquals(i, data.getEmployee(i).getID());
      assertEquals("employee" + i, data.getEmployee(i).getName());
      assertEquals(i, data.getEmployee(i).getDepartment());
      assertTrue(data.getEmployee(i).isAdmin());
    }
    assertThrows(RuntimeException.class, () -> data.getEmployee(0));
    assertThrows(RuntimeException.class, () -> data.getEmployee(50));
  }

  @Test
  @Order(6)
  void Many_OverWrite_plusBoundary()
  {
    assertThrows(RuntimeException.class, () -> data.getEmployee(1));
    for (int i = 1; i < 50; i++)
    {
      data.employee(i, "employee" + i, i, true);
      waitForRunLater();
    }
    for (int i = 1; i < 50; i++)
    {
      assertEquals(i, data.getEmployee(i).getID());
      assertEquals("employee" + i, data.getEmployee(i).getName());
      assertEquals(i, data.getEmployee(i).getDepartment());
      assertTrue(data.getEmployee(i).isAdmin());
    }
    for (int i = 1; i < 50; i++)
    {
      data.employee(i, "employeeChanged" + i, i * 10, false);
      waitForRunLater();
    }
    for (int i = 1; i < 50; i++)
    {
      assertEquals(i, data.getEmployee(i).getID());
      assertEquals("employeeChanged" + i, data.getEmployee(i).getName());
      assertEquals(i * 10, data.getEmployee(i).getDepartment());
      assertFalse(data.getEmployee(i).isAdmin());
    }
    assertThrows(RuntimeException.class, () -> data.getEmployee(0));
    assertThrows(RuntimeException.class, () -> data.getEmployee(50));
  }

  @Test
  @Order(7)
  void Many_Remove_plusBoundary()
  {
    assertThrows(RuntimeException.class, () -> data.getEmployee(1));
    for (int i = 1; i < 50; i++)
    {
      data.employee(i, "employee" + i, i, true);
      waitForRunLater();
    }
    for (int i = 1; i < 50; i++)
    {
      assertEquals(i, data.getEmployee(i).getID());
      assertEquals("employee" + i, data.getEmployee(i).getName());
      assertEquals(i, data.getEmployee(i).getDepartment());
      assertTrue(data.getEmployee(i).isAdmin());
    }
    for (int i = 1; i < 50; i += 2)
    {
      data.removeEmployee(i);
      waitForRunLater();
    }
    for (int i = 1; i < 50; i += 2)
    {
      int finalI = i;
      assertThrows(RuntimeException.class, () -> data.getEmployee(finalI));
    }
    for (int i = 2; i < 50; i += 2)
    {
      assertEquals(i, data.getEmployee(i).getID());
      assertEquals("employee" + i, data.getEmployee(i).getName());
      assertEquals(i, data.getEmployee(i).getDepartment());
      assertTrue(data.getEmployee(i).isAdmin());
    }
    assertThrows(RuntimeException.class, () -> data.getEmployee(0));
    assertThrows(RuntimeException.class, () -> data.getEmployee(50));
  }

  private void waitForRunLater()
  {
    try
    {
      Semaphore semaphore = new Semaphore(0);
      Platform.runLater(semaphore::release);
      semaphore.acquire();
    }
    catch (InterruptedException e)
    {
      throw new RuntimeException(e);
    }
  }
}
