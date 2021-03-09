package viewmodel;

public class ViewModelFactory
{
  private LoginViewModel loginViewModel;
  private EditEmployeeViewModel editEmployeeViewModel;
  private EditEmployeeSelectViewModel editEmployeeSelectViewModel;
  private EmployeeSpinnyBarViewModel employeeSpinnyBarViewModel;
  private MainMenuViewModel mainMenuViewModel;
  private EditDepartmentViewModel editDepartmentViewModel;
  private EditDepartmentSelectViewModel editDepartmentSelectViewModel;
  private DepartmentSpinnyBarViewModel departmentSpinnyBarViewModel;
  private GroupChatViewModel groupChatViewModel;
  private PrivateChatViewModel privateChatViewModel;
  private EditGroupChatViewModel editGroupChatViewModel;

  public ViewModelFactory()
  {
    loginViewModel = new LoginViewModel();
    editEmployeeViewModel = new EditEmployeeViewModel();
    editEmployeeSelectViewModel = new EditEmployeeSelectViewModel();
    employeeSpinnyBarViewModel = new EmployeeSpinnyBarViewModel();
    mainMenuViewModel = new MainMenuViewModel();
    editDepartmentViewModel = new EditDepartmentViewModel();
    editDepartmentSelectViewModel = new EditDepartmentSelectViewModel();
    departmentSpinnyBarViewModel = new DepartmentSpinnyBarViewModel();
    groupChatViewModel = new GroupChatViewModel();
    privateChatViewModel = new PrivateChatViewModel();
    editGroupChatViewModel = new EditGroupChatViewModel();
  }

  public LoginViewModel getLoginViewModel()
  {
    return loginViewModel;
  }

  public EditEmployeeViewModel getEditEmployeeViewModel()
  {
    return editEmployeeViewModel;
  }

  public EditEmployeeSelectViewModel getEditEmployeeSelectViewModel()
  {
    return editEmployeeSelectViewModel;
  }

  public EmployeeSpinnyBarViewModel getEmployeeSpinnyBarViewModel()
  {
    return employeeSpinnyBarViewModel;
  }

  public MainMenuViewModel getMainMenuViewModel()
  {
    return mainMenuViewModel;
  }

  public EditDepartmentViewModel getEditDepartmentViewModel()
  {
    return editDepartmentViewModel;
  }

  public EditDepartmentSelectViewModel getEditDepartmentSelectViewModel()
  {
    return editDepartmentSelectViewModel;
  }

  public DepartmentSpinnyBarViewModel getDepartmentSpinnyBarViewModel()
  {
    return departmentSpinnyBarViewModel;
  }

  public GroupChatViewModel getGroupChatViewModel()
  {
    return groupChatViewModel;
  }

  public PrivateChatViewModel getPrivateChatViewModel()
  {
    return privateChatViewModel;
  }

  public EditGroupChatViewModel getEditGroupChatViewModel()
  {
    return editGroupChatViewModel;
  }
}
