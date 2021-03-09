package model.business;

import model.ServerManager;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class _test_ChatManager
{
  private ChatManager manager;

  @BeforeAll
  void init()
  {
    ServerManager.init(null);
  }

  @BeforeEach
  void setUp()
  {
    System.out.println("---> set up");
    manager = new ChatManager();
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
    assertThrows(NullPointerException.class, () -> manager.cloneChat(0));
  }

  @Test
  @Order(1)
  void One_Load_and_Clone_plusBoundary()
  {
    Chat emp1 = new Chat(1, "chat1", true, new int[2]);
    manager.loadChat(emp1);
    assertThrows(NullPointerException.class, () -> manager.cloneChat(0));
    assertThrows(NullPointerException.class, () -> manager.cloneChat(2));
    Chat emp2 = manager.cloneChat(1);
    assertEquals(emp1.getID(), emp2.getID());
    assertEquals(emp1.getName(), emp2.getName());
    assertEquals(emp1.isGroup(), emp2.isGroup());
    assertEquals(emp1.getMembers().length, emp2.getMembers().length);
  }

  @Test
  @Order(2)
  void Many_Load_and_Clone_plusBoundary()
  {
    for (int i = 1; i < 50; i++)
    {
      Chat emp1 = new Chat(i, "chat" + i, true, new int[i]);
      manager.loadChat(emp1);
    }
    assertThrows(NullPointerException.class, () -> manager.cloneChat(0));
    assertThrows(NullPointerException.class, () -> manager.cloneChat(50));
    for (int i = 1; i < 50; i++)
    {
      Chat emp2 = manager.cloneChat(i);
      assertEquals(i, emp2.getID());
      assertEquals("chat" + i, emp2.getName());
      assertTrue(emp2.isGroup());
      assertEquals(i, emp2.getMembers().length);
    }
  }

  @Test
  @Order(3)
  void One_OverWrite()
  {
    Chat emp1 = new Chat(1, "chat1", true, new int[2]);
    manager.loadChat(emp1);
    Chat emp2 = manager.cloneChat(1);
    assertEquals(emp1.getID(), emp2.getID());
    assertEquals(emp1.getName(), emp2.getName());
    assertEquals(emp1.isGroup(), emp2.isGroup());
    assertEquals(emp1.getMembers().length, emp2.getMembers().length);

    emp1 = new Chat(1, "chat2", false, new int[3]);
    manager.loadChat(emp1);
    emp2 = manager.cloneChat(1);
    assertEquals(emp1.getID(), emp2.getID());
    assertEquals(emp1.getName(), emp2.getName());
    assertEquals(emp1.isGroup(), emp2.isGroup());
    assertEquals(emp1.getMembers().length, emp2.getMembers().length);
  }

  @Test
  @Order(4)
  void Many_OverWrite()
  {
    for (int i = 1; i < 50; i++)
    {
      Chat emp1 = new Chat(i, "chat" + i, true, new int[i]);
      manager.loadChat(emp1);
    }
    for (int i = 1; i < 50; i++)
    {
      Chat emp2 = manager.cloneChat(i);
      assertEquals(i, emp2.getID());
      assertEquals("chat" + i, emp2.getName());
      assertTrue(emp2.isGroup());
      assertEquals(i, emp2.getMembers().length);
    }

    for (int i = 1; i < 50; i += 2)
    {
      Chat emp1 = new Chat(i, "chatChanged" + i, false, new int[i * 10]);
      manager.loadChat(emp1);
    }
    for (int i = 1; i < 50; i += 2)
    {
      Chat emp2 = manager.cloneChat(i);
      assertEquals(i, emp2.getID());
      assertEquals("chatChanged" + i, emp2.getName());
      assertFalse(emp2.isGroup());
      assertEquals(i * 10, emp2.getMembers().length);
    }
    for (int i = 2; i < 50; i += 2)
    {
      Chat emp2 = manager.cloneChat(i);
      assertEquals(i, emp2.getID());
      assertEquals("chat" + i, emp2.getName());
      assertTrue(emp2.isGroup());
      assertEquals(i, emp2.getMembers().length);
    }
  }

  @Test
  @Order(5)
  void Method_getGroupChats()
  {
    for (int i = 0; i < 10; i++)
    {
      Chat emp1 = new Chat(i, "chat" + i, true, new int[]{1});
      manager.loadChat(emp1);
    }
    for (int i = 10; i < 50; i ++)
    {
      Chat emp1 = new Chat(i, "chat" + i, false, new int[]{1});
      manager.loadChat(emp1);
    }
    assertEquals(10, manager.getGroupChats(1).length);
  }

  @Test
  @Order(6)
  void Method_getPrivateChats()
  {
    for (int i = 0; i < 10; i++)
    {
      Chat emp1 = new Chat(i, "chat" + i, false, new int[]{1});
      manager.loadChat(emp1);
    }
    for (int i = 10; i < 50; i ++)
    {
      Chat emp1 = new Chat(i, "chat" + i, true, new int[]{1});
      manager.loadChat(emp1);
    }
    assertEquals(10, manager.getPrivateChats(1).length);
  }
}
