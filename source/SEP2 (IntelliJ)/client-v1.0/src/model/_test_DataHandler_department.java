package model;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.*;

import java.util.concurrent.Semaphore;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class _test_DataHandler_department
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
    assertThrows(RuntimeException.class, () -> data.getDepartment(-1));
    assertEquals(0, data.getDepartment(0).getID());
    assertEquals("No Department", data.getDepartment(0).getName());
    assertThrows(RuntimeException.class, () -> data.getDepartment(1));
    assertThrows(RuntimeException.class, () -> data.getDepartment(2));
  }

  @Test
  @Order(1)
  void One_Add()
  {
    data.department(1,"department1");
  }

  @Test
  @Order(2)
  void One_Get_plusBoundary()
  {
    assertThrows(RuntimeException.class, () -> data.getDepartment(1));
    data.department(1,"department1");
    waitForRunLater();
    assertEquals(1, data.getDepartment(1).getID());
    assertEquals("department1", data.getDepartment(1).getName());
    assertThrows(RuntimeException.class, () -> data.getDepartment(-1));
    assertEquals(0, data.getDepartment(0).getID());
    assertEquals("No Department", data.getDepartment(0).getName());
    assertThrows(RuntimeException.class, () -> data.getDepartment(2));
  }

  @Test
  @Order(3)
  void One_OverWrite_plusBoundary()
  {
    assertThrows(RuntimeException.class, () -> data.getDepartment(1));
    data.department(1,"department1");
    waitForRunLater();
    assertEquals(1, data.getDepartment(1).getID());
    assertEquals("department1", data.getDepartment(1).getName());
    data.department(1,"department2");
    waitForRunLater();
    assertEquals(1, data.getDepartment(1).getID());
    assertEquals("department2", data.getDepartment(1).getName());
    assertThrows(RuntimeException.class, () -> data.getDepartment(-1));
    assertEquals(0, data.getDepartment(0).getID());
    assertEquals("No Department", data.getDepartment(0).getName());
    assertThrows(RuntimeException.class, () -> data.getDepartment(2));
  }

  @Test
  @Order(4)
  void One_Remove_plusBoundary()
  {
    assertThrows(RuntimeException.class, () -> data.getDepartment(1));
    data.department(1,"department1");
    waitForRunLater();
    data.department(2,"department2");
    waitForRunLater();
    assertEquals(1, data.getDepartment(1).getID());
    assertEquals("department1", data.getDepartment(1).getName());
    assertEquals(2, data.getDepartment(2).getID());
    assertEquals("department2", data.getDepartment(2).getName());
    assertThrows(RuntimeException.class, () -> data.getDepartment(-1));
    assertEquals(0, data.getDepartment(0).getID());
    assertEquals("No Department", data.getDepartment(0).getName());
    assertThrows(RuntimeException.class, () -> data.getDepartment(3));
    data.removeDepartment(2);
    waitForRunLater();
    assertEquals(1, data.getDepartment(1).getID());
    assertEquals("department1", data.getDepartment(1).getName());
    assertThrows(RuntimeException.class, () -> data.getDepartment(2));
    assertThrows(RuntimeException.class, () -> data.getDepartment(-1));
    assertEquals(0, data.getDepartment(0).getID());
    assertEquals("No Department", data.getDepartment(0).getName());
    assertThrows(RuntimeException.class, () -> data.getDepartment(3));
  }

  @Test
  @Order(5)
  void Many_Get_plusBoundary()
  {
    assertThrows(RuntimeException.class, () -> data.getDepartment(1));
    for (int i = 1; i < 50; i++)
    {
      data.department(i, "department" + i);
      waitForRunLater();
    }
    for (int i = 1; i < 50; i++)
    {
      assertEquals(i, data.getDepartment(i).getID());
      assertEquals("department" + i, data.getDepartment(i).getName());
    }
    assertThrows(RuntimeException.class, () -> data.getDepartment(-1));
    assertEquals(0, data.getDepartment(0).getID());
    assertEquals("No Department", data.getDepartment(0).getName());
    assertThrows(RuntimeException.class, () -> data.getDepartment(50));
  }

  @Test
  @Order(6)
  void Many_OverWrite_plusBoundary()
  {
    assertThrows(RuntimeException.class, () -> data.getDepartment(1));
    for (int i = 1; i < 50; i++)
    {
      data.department(i, "department" + i);
      waitForRunLater();
    }
    for (int i = 1; i < 50; i++)
    {
      assertEquals(i, data.getDepartment(i).getID());
      assertEquals("department" + i, data.getDepartment(i).getName());
    }
    for (int i = 1; i < 50; i++)
    {
      data.department(i, "departmentChanged" + i);
      waitForRunLater();
    }
    for (int i = 1; i < 50; i++)
    {
      assertEquals(i, data.getDepartment(i).getID());
      assertEquals("departmentChanged" + i, data.getDepartment(i).getName());
    }
    assertThrows(RuntimeException.class, () -> data.getDepartment(-1));
    assertEquals(0, data.getDepartment(0).getID());
    assertEquals("No Department", data.getDepartment(0).getName());
    assertThrows(RuntimeException.class, () -> data.getDepartment(50));
  }

  @Test
  @Order(7)
  void Many_Remove_plusBoundary()
  {
    assertThrows(RuntimeException.class, () -> data.getDepartment(1));
    for (int i = 1; i < 50; i++)
    {
      data.department(i, "department" + i);
      waitForRunLater();
    }
    for (int i = 1; i < 50; i++)
    {
      assertEquals(i, data.getDepartment(i).getID());
      assertEquals("department" + i, data.getDepartment(i).getName());
    }
    for (int i = 1; i < 50; i += 2)
    {
      data.removeDepartment(i);
      waitForRunLater();
    }
    for (int i = 1; i < 50; i += 2)
    {
      int finalI = i;
      assertThrows(RuntimeException.class, () -> data.getDepartment(finalI));
    }
    for (int i = 2; i < 50; i += 2)
    {
      assertEquals(i, data.getDepartment(i).getID());
      assertEquals("department" + i, data.getDepartment(i).getName());
    }
    assertThrows(RuntimeException.class, () -> data.getDepartment(-1));
    assertEquals(0, data.getDepartment(0).getID());
    assertEquals("No Department", data.getDepartment(0).getName());
    assertThrows(RuntimeException.class, () -> data.getDepartment(50));
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
