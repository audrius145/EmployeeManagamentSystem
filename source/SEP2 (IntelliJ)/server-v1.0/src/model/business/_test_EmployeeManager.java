package model.business;

import model.ServerManager;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class _test_EmployeeManager
{
  private EmployeeManager manager;

  @BeforeAll
  void init()
  {
    ServerManager.init(null);
  }

  @BeforeEach
  void setUp()
  {
    System.out.println("---> set up");
    manager = new EmployeeManager();
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
    assertThrows(NullPointerException.class, () -> manager.cloneEmployee(0));
  }

  @Test
  @Order(1)
  void One_Load_and_Clone_plusBoundary()
  {
    Employee emp1 = new Employee(1, "employee1", 1, true, "pwd1", false);
    manager.loadEmployee(emp1);
    assertThrows(NullPointerException.class, () -> manager.cloneEmployee(0));
    assertThrows(NullPointerException.class, () -> manager.cloneEmployee(2));
    Employee emp2 = manager.cloneEmployee(1);
    assertEquals(emp1.getID(), emp2.getID());
    assertEquals(emp1.getName(), emp2.getName());
    assertEquals(emp1.getDepartment(), emp2.getDepartment());
    assertEquals(emp1.isAdmin(), emp2.isAdmin());
    assertEquals(emp1.getPassword(), emp2.getPassword());
    assertEquals(emp1.isArchived(), emp2.isArchived());
  }

  @Test
  @Order(2)
  void Many_Load_and_Clone_plusBoundary()
  {
    for (int i = 1; i < 50; i++)
    {
      Employee emp1 = new Employee(i, "employee" + i, i, true, "pwd" + i, false);
      manager.loadEmployee(emp1);
    }
    assertThrows(NullPointerException.class, () -> manager.cloneEmployee(0));
    assertThrows(NullPointerException.class, () -> manager.cloneEmployee(50));
    for (int i = 1; i < 50; i++)
    {
      Employee emp2 = manager.cloneEmployee(i);
      assertEquals(i, emp2.getID());
      assertEquals("employee" + i, emp2.getName());
      assertEquals(i, emp2.getDepartment());
      assertTrue(emp2.isAdmin());
      assertEquals("pwd" + i, emp2.getPassword());
      assertFalse(emp2.isArchived());
    }
  }

  @Test
  @Order(3)
  void One_OverWrite()
  {
    Employee emp1 = new Employee(1, "employee1", 1, true, "pwd1", false);
    manager.loadEmployee(emp1);
    Employee emp2 = manager.cloneEmployee(1);
    assertEquals(emp1.getID(), emp2.getID());
    assertEquals(emp1.getName(), emp2.getName());
    assertEquals(emp1.getDepartment(), emp2.getDepartment());
    assertEquals(emp1.isAdmin(), emp2.isAdmin());
    assertEquals(emp1.getPassword(), emp2.getPassword());
    assertEquals(emp1.isArchived(), emp2.isArchived());

    emp1 = new Employee(1, "employee2", 2, false, "pwd2", false);
    manager.loadEmployee(emp1);
    emp2 = manager.cloneEmployee(1);
    assertEquals(emp1.getID(), emp2.getID());
    assertEquals(emp1.getName(), emp2.getName());
    assertEquals(emp1.getDepartment(), emp2.getDepartment());
    assertEquals(emp1.isAdmin(), emp2.isAdmin());
    assertEquals(emp1.getPassword(), emp2.getPassword());
    assertEquals(emp1.isArchived(), emp2.isArchived());
  }

  @Test
  @Order(4)
  void Many_OverWrite()
  {
    for (int i = 1; i < 50; i++)
    {
      Employee emp1 = new Employee(i, "employee" + i, i, true, "pwd" + i, false);
      manager.loadEmployee(emp1);
    }
    for (int i = 1; i < 50; i++)
    {
      Employee emp2 = manager.cloneEmployee(i);
      assertEquals(i, emp2.getID());
      assertEquals("employee" + i, emp2.getName());
      assertEquals(i, emp2.getDepartment());
      assertTrue(emp2.isAdmin());
      assertEquals("pwd" + i, emp2.getPassword());
      assertFalse(emp2.isArchived());
    }

    for (int i = 1; i < 50; i += 2)
    {
      Employee emp1 = new Employee(i, "employeeChanged" + i, i * 10, false, "pwdChanged" + i, false);
      manager.loadEmployee(emp1);
    }
    for (int i = 1; i < 50; i += 2)
    {
      Employee emp2 = manager.cloneEmployee(i);
      assertEquals(i, emp2.getID());
      assertEquals("employeeChanged" + i, emp2.getName());
      assertEquals(i * 10, emp2.getDepartment());
      assertFalse(emp2.isAdmin());
      assertEquals("pwdChanged" + i, emp2.getPassword());
      assertFalse(emp2.isArchived());
    }
    for (int i = 2; i < 50; i += 2)
    {
      Employee emp2 = manager.cloneEmployee(i);
      assertEquals(i, emp2.getID());
      assertEquals("employee" + i, emp2.getName());
      assertEquals(i, emp2.getDepartment());
      assertTrue(emp2.isAdmin());
      assertEquals("pwd" + i, emp2.getPassword());
      assertFalse(emp2.isArchived());
    }
  }

  @Test
  @Order(5)
  void Method_getActive()
  {
    for (int i = 0; i < 10; i++)
    {
      Employee emp1 = new Employee(i, "employee" + i, i, true, "pwd" + i, false);
      manager.loadEmployee(emp1);
    }
    for (int i = 10; i < 50; i ++)
    {
      Employee emp1 = new Employee(i, "employee" + i, i, true, "pwd" + i, true);
      manager.loadEmployee(emp1);
    }
    assertEquals(10, manager.getActiveEmployees().length);
  }

  @Test
  @Order(6)
  void Method_getAll()
  {
    for (int i = 0; i < 10; i++)
    {
      Employee emp1 = new Employee(i, "employee" + i, i, true, "pwd" + i, false);
      manager.loadEmployee(emp1);
    }
    for (int i = 10; i < 50; i ++)
    {
      Employee emp1 = new Employee(i, "employee" + i, i, true, "pwd" + i, true);
      manager.loadEmployee(emp1);
    }
    assertEquals(50, manager.getAllEmployees().length);
  }

  @Test
  @Order(7)
  void Method_acquireEmployee_plusBoundary()
  {
    Employee emp1 = new Employee(1, "employee1", 1, true, "pwd1", false);
    manager.loadEmployee(emp1);
    assertThrows(NullPointerException.class, () -> manager.acquireEmployee(0));
    assertThrows(NullPointerException.class, () -> manager.acquireEmployee(2));
    EmployeeEdit proxy = manager.acquireEmployee(1);
    assertTrue(proxy.isAcquired());
    assertFalse(proxy.isPassword("employee2"));
    proxy.setPassword("employee2");
    assertTrue(proxy.isPassword("employee2"));
    manager.releaseAll();
    assertFalse(proxy.isAcquired());
  }
}
