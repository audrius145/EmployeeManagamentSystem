package model.business;

import model.ServerManager;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class _test_DepartmentManager
{
  private DepartmentManager manager;

  @BeforeAll
  void init()
  {
    ServerManager.init(null);
  }

  @BeforeEach
  void setUp()
  {
    System.out.println("---> set up");
    manager = new DepartmentManager();
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
    assertThrows(NullPointerException.class, () -> manager.cloneDepartment(0));
  }

  @Test
  @Order(1)
  void One_Load_and_Clone_plusBoundary()
  {
    Department emp1 = new Department(1, "department1");
    manager.loadDepartment(emp1);
    assertThrows(NullPointerException.class, () -> manager.cloneDepartment(0));
    assertThrows(NullPointerException.class, () -> manager.cloneDepartment(2));
    Department emp2 = manager.cloneDepartment(1);
    assertEquals(emp1.getID(), emp2.getID());
    assertEquals(emp1.getName(), emp2.getName());
  }

  @Test
  @Order(2)
  void Many_Load_and_Clone_plusBoundary()
  {
    for (int i = 1; i < 50; i++)
    {
      Department emp1 = new Department(i, "department" + i);
      manager.loadDepartment(emp1);
    }
    assertThrows(NullPointerException.class, () -> manager.cloneDepartment(0));
    assertThrows(NullPointerException.class, () -> manager.cloneDepartment(50));
    for (int i = 1; i < 50; i++)
    {
      Department emp2 = manager.cloneDepartment(i);
      assertEquals(i, emp2.getID());
      assertEquals("department" + i, emp2.getName());
    }
  }

  @Test
  @Order(3)
  void One_OverWrite()
  {
    Department emp1 = new Department(1, "department1");
    manager.loadDepartment(emp1);
    Department emp2 = manager.cloneDepartment(1);
    assertEquals(emp1.getID(), emp2.getID());
    assertEquals(emp1.getName(), emp2.getName());

    emp1 = new Department(1, "department2");
    manager.loadDepartment(emp1);
    emp2 = manager.cloneDepartment(1);
    assertEquals(emp1.getID(), emp2.getID());
    assertEquals(emp1.getName(), emp2.getName());
  }

  @Test
  @Order(4)
  void Many_OverWrite()
  {
    for (int i = 1; i < 50; i++)
    {
      Department emp1 = new Department(i, "department" + i);
      manager.loadDepartment(emp1);
    }
    for (int i = 1; i < 50; i++)
    {
      Department emp2 = manager.cloneDepartment(i);
      assertEquals(i, emp2.getID());
      assertEquals("department" + i, emp2.getName());
    }

    for (int i = 1; i < 50; i += 2)
    {
      Department emp1 = new Department(i, "departmentChanged" + i);
      manager.loadDepartment(emp1);
    }
    for (int i = 1; i < 50; i += 2)
    {
      Department emp2 = manager.cloneDepartment(i);
      assertEquals(i, emp2.getID());
      assertEquals("departmentChanged" + i, emp2.getName());
    }
    for (int i = 2; i < 50; i += 2)
    {
      Department emp2 = manager.cloneDepartment(i);
      assertEquals(i, emp2.getID());
      assertEquals("department" + i, emp2.getName());
    }
  }

  @Test
  @Order(5)
  void Method_getDepartments()
  {
    for (int i = 0; i < 50; i ++)
    {
      Department emp1 = new Department(i, "department" + i);
      manager.loadDepartment(emp1);
    }
    assertEquals(50, manager.getDepartments().length);
  }

  @Test
  @Order(6)
  void Method_acquireDepartment_plusBoundary()
  {
    Department emp1 = new Department(1, "department1");
    manager.loadDepartment(emp1);
    assertThrows(NullPointerException.class, () -> manager.acquireDepartment(0));
    assertThrows(NullPointerException.class, () -> manager.acquireDepartment(2));
    DepartmentEdit proxy = manager.acquireDepartment(1);
    assertTrue(proxy.isAcquired());
    assertEquals("department1", proxy.getName());
    proxy.update("department2");
    assertEquals("department2", proxy.getName());
    manager.releaseAll();
    assertFalse(proxy.isAcquired());
  }
}
