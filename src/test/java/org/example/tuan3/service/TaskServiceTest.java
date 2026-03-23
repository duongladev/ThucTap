package org.example.tuan3.service;

import org.example.tuan3.dto.request.AssignTaskRequest;
import org.example.tuan3.dto.request.CreateTaskRequest;
import org.example.tuan3.dto.request.UpdateTaskStatusRequest;
import org.example.tuan3.dto.response.TaskResponse;
import org.example.tuan3.entity.ProjectEntity;
import org.example.tuan3.entity.TaskEntity;
import org.example.tuan3.entity.UserEntity;
import org.example.tuan3.enums.TaskPriority;
import org.example.tuan3.enums.TaskStatus;
import org.example.tuan3.exception.BadRequestException;
import org.example.tuan3.exception.ResourceNotFoundException;
import org.example.tuan3.repository.ProjectRepository;
import org.example.tuan3.repository.TaskRepository;
import org.example.tuan3.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    private ProjectEntity project;
    private UserEntity activeUser;
    private UserEntity inactiveUser;
    private TaskEntity todoTask;
    private TaskEntity inProgressTask;
    private TaskEntity doneTask;

    @BeforeEach
    void setUp() {
        project = buildProject(1, "Project A");

        activeUser = buildUser(2, "Nguyen Van A", true);
        inactiveUser = buildUser(3, "Tran Thi B", false);

        todoTask = buildTask(10, "Task TODO", TaskStatus.TODO, TaskPriority.MEDIUM, project, null);
        inProgressTask = buildTask(11, "Task Doing", TaskStatus.IN_PROGRESS, TaskPriority.HIGH, project, activeUser);
        doneTask = buildTask(12, "Task Done", TaskStatus.DONE, TaskPriority.LOW, project, activeUser);
    }

    @Test
    void createTask_shouldCreateSuccessfully_whenProjectExists() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Implement Task API");
        request.setDescription("Create task endpoint");
        request.setPriority(TaskPriority.HIGH);
        request.setDueDate(LocalDate.now().plusDays(3));
        request.setProjectId(1);

        when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        when(taskRepository.save(any(TaskEntity.class))).thenAnswer(invocation -> {
            TaskEntity savedTask = invocation.getArgument(0);
            savedTask.setId(100);
            return savedTask;
        });

        TaskResponse response = taskService.createTask(request);

        assertNotNull(response);
        assertEquals(100, response.getId());
        assertEquals("Implement Task API", response.getTitle());
        assertEquals(TaskPriority.HIGH, response.getPriority());
        assertEquals(TaskStatus.TODO, response.getStatus());
        assertEquals(1, response.getProjectId());

        ArgumentCaptor<TaskEntity> taskCaptor = ArgumentCaptor.forClass(TaskEntity.class);
        verify(taskRepository).save(taskCaptor.capture());

        TaskEntity savedTask = taskCaptor.getValue();
        assertEquals("Implement Task API", savedTask.getTitle());
        assertEquals("Create task endpoint", savedTask.getDescription());
        assertEquals(TaskPriority.HIGH, savedTask.getPriority());
        assertEquals(TaskStatus.TODO, savedTask.getStatus());
        assertEquals(project, savedTask.getProject());
        assertNotNull(savedTask.getCreatedAt());
        assertNotNull(savedTask.getUpdatedAt());
    }

    @Test
    void createTask_shouldUseMediumPriority_whenPriorityIsNull() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Task no priority");
        request.setDescription("Test default priority");
        request.setDueDate(LocalDate.now().plusDays(5));
        request.setProjectId(1);

        when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        when(taskRepository.save(any(TaskEntity.class))).thenAnswer(invocation -> {
            TaskEntity savedTask = invocation.getArgument(0);
            savedTask.setId(101);
            return savedTask;
        });

        TaskResponse response = taskService.createTask(request);

        assertEquals(TaskPriority.MEDIUM, response.getPriority());
        assertEquals(TaskStatus.TODO, response.getStatus());

        verify(taskRepository).save(any(TaskEntity.class));
    }

    @Test
    void createTask_shouldThrowNotFound_whenProjectDoesNotExist() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Invalid project task");
        request.setProjectId(999);

        when(projectRepository.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> taskService.createTask(request)
        );

        assertEquals("Project not found with id: 999", exception.getMessage());
        verify(taskRepository, never()).save(any());
    }

    @Test
    void assignTask_shouldAssignSuccessfully_whenInputIsValid() {
        AssignTaskRequest request = new AssignTaskRequest();
        request.setUserId(2);

        when(taskRepository.findById(10)).thenReturn(Optional.of(todoTask));
        when(userRepository.findById(2)).thenReturn(Optional.of(activeUser));
        when(projectRepository.countMemberInProject(1, 2)).thenReturn(1L);
        when(taskRepository.save(any(TaskEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskResponse response = taskService.assignTask(10, request);

        assertNotNull(response);
        assertEquals(2, response.getAssigneeId());
        assertEquals("Nguyen Van A", response.getAssigneeName());

        verify(taskRepository).findById(10);
        verify(userRepository).findById(2);
        verify(projectRepository).countMemberInProject(1, 2);
        verify(taskRepository).save(todoTask);

        assertEquals(activeUser, todoTask.getAssignee());
        assertNotNull(todoTask.getUpdatedAt());
    }

    @Test
    void assignTask_shouldThrowNotFound_whenTaskDoesNotExist() {
        AssignTaskRequest request = new AssignTaskRequest();
        request.setUserId(2);

        when(taskRepository.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> taskService.assignTask(999, request)
        );

        assertEquals("Task not found with id: 999", exception.getMessage());
        verify(userRepository, never()).findById(any());
        verify(taskRepository, never()).save(any());
    }

    @Test
    void assignTask_shouldThrowBadRequest_whenTaskIsDone() {
        AssignTaskRequest request = new AssignTaskRequest();
        request.setUserId(2);

        when(taskRepository.findById(12)).thenReturn(Optional.of(doneTask));

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> taskService.assignTask(12, request)
        );

        assertEquals("Task is DONE, cannot assign", exception.getMessage());
        verify(userRepository, never()).findById(any());
        verify(taskRepository, never()).save(any());
    }

    @Test
    void assignTask_shouldThrowNotFound_whenUserDoesNotExist() {
        AssignTaskRequest request = new AssignTaskRequest();
        request.setUserId(999);

        when(taskRepository.findById(10)).thenReturn(Optional.of(todoTask));
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> taskService.assignTask(10, request)
        );

        assertEquals("User not found with id: 999", exception.getMessage());
        verify(projectRepository, never()).countMemberInProject(anyInt(), anyInt());
        verify(taskRepository, never()).save(any());
    }

    @Test
    void assignTask_shouldThrowBadRequest_whenUserIsInactive() {
        AssignTaskRequest request = new AssignTaskRequest();
        request.setUserId(3);

        when(taskRepository.findById(10)).thenReturn(Optional.of(todoTask));
        when(userRepository.findById(3)).thenReturn(Optional.of(inactiveUser));

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> taskService.assignTask(10, request)
        );

        assertEquals("User is inactive, cannot assign task", exception.getMessage());
        verify(projectRepository, never()).countMemberInProject(anyInt(), anyInt());
        verify(taskRepository, never()).save(any());
    }

    @Test
    void assignTask_shouldThrowBadRequest_whenUserDoesNotBelongToProject() {
        AssignTaskRequest request = new AssignTaskRequest();
        request.setUserId(2);

        when(taskRepository.findById(10)).thenReturn(Optional.of(todoTask));
        when(userRepository.findById(2)).thenReturn(Optional.of(activeUser));
        when(projectRepository.countMemberInProject(1, 2)).thenReturn(0L);

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> taskService.assignTask(10, request)
        );

        assertEquals("User does not belong to this project", exception.getMessage());
        verify(taskRepository, never()).save(any());
    }

    @Test
    void updateStatus_shouldUpdateToInProgress_whenCurrentStatusIsTodo() {
        UpdateTaskStatusRequest request = new UpdateTaskStatusRequest();
        request.setStatus(TaskStatus.IN_PROGRESS);

        when(taskRepository.findById(10)).thenReturn(Optional.of(todoTask));
        when(taskRepository.save(any(TaskEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskResponse response = taskService.updateStatus(10, request);

        assertEquals(TaskStatus.IN_PROGRESS, response.getStatus());
        verify(taskRepository).save(todoTask);
    }

    @Test
    void updateStatus_shouldUpdateToDone_whenCurrentStatusIsInProgress() {
        UpdateTaskStatusRequest request = new UpdateTaskStatusRequest();
        request.setStatus(TaskStatus.DONE);

        when(taskRepository.findById(11)).thenReturn(Optional.of(inProgressTask));
        when(taskRepository.save(any(TaskEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskResponse response = taskService.updateStatus(11, request);

        assertEquals(TaskStatus.DONE, response.getStatus());
        verify(taskRepository).save(inProgressTask);
    }

    @Test
    void updateStatus_shouldThrowBadRequest_whenTaskAlreadyDone() {
        UpdateTaskStatusRequest request = new UpdateTaskStatusRequest();
        request.setStatus(TaskStatus.TODO);

        when(taskRepository.findById(12)).thenReturn(Optional.of(doneTask));

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> taskService.updateStatus(12, request)
        );

        assertEquals("Task is DONE, status cannot be changed", exception.getMessage());
        verify(taskRepository, never()).save(any());
    }

    @Test
    void updateStatus_shouldThrowBadRequest_whenTransitionIsInvalid() {
        UpdateTaskStatusRequest request = new UpdateTaskStatusRequest();
        request.setStatus(TaskStatus.DONE);

        when(taskRepository.findById(10)).thenReturn(Optional.of(todoTask));

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> taskService.updateStatus(10, request)
        );

        assertEquals("Task can only move from TODO to IN_PROGRESS", exception.getMessage());
        verify(taskRepository, never()).save(any());
    }

    @Test
    void getTasksByProject_shouldReturnList_whenProjectExists() {
        when(projectRepository.existsById(1)).thenReturn(true);
        when(taskRepository.findByProject_Id(1)).thenReturn(List.of(todoTask, inProgressTask));

        List<TaskResponse> responses = taskService.getTasksByProject(1);

        assertEquals(2, responses.size());
        verify(projectRepository).existsById(1);
        verify(taskRepository).findByProject_Id(1);
    }

    @Test
    void getTasksByProject_shouldThrowNotFound_whenProjectDoesNotExist() {
        when(projectRepository.existsById(999)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> taskService.getTasksByProject(999)
        );

        assertEquals("Project not found with id: 999", exception.getMessage());
        verify(taskRepository, never()).findByProject_Id(anyInt());
    }

    @Test
    void getTasksByUser_shouldReturnList_whenUserExists() {
        when(userRepository.existsById(2)).thenReturn(true);
        when(taskRepository.findByAssignee_Id(2)).thenReturn(List.of(inProgressTask, doneTask));

        List<TaskResponse> responses = taskService.getTasksByUser(2);

        assertEquals(2, responses.size());
        verify(userRepository).existsById(2);
        verify(taskRepository).findByAssignee_Id(2);
    }

    @Test
    void getTasksByUser_shouldThrowNotFound_whenUserDoesNotExist() {
        when(userRepository.existsById(999)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> taskService.getTasksByUser(999)
        );

        assertEquals("User not found with id: 999", exception.getMessage());
        verify(taskRepository, never()).findByAssignee_Id(anyInt());
    }

    private ProjectEntity buildProject(Integer id, String name) {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(id);
        projectEntity.setName(name);
        projectEntity.setDescription("Demo project");
        projectEntity.setCreatedAt(LocalDateTime.now());
        projectEntity.setUpdatedAt(LocalDateTime.now());
        return projectEntity;
    }

    private UserEntity buildUser(Integer id, String fullName, Boolean active) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setFullName(fullName);
        userEntity.setEmail(fullName.toLowerCase().replace(" ", "") + "@gmail.com");
        userEntity.setPhone("0900000000");
        userEntity.setActive(active);
        userEntity.setCreatedAt(LocalDateTime.now());
        return userEntity;
    }

    private TaskEntity buildTask(
            Integer id,
            String title,
            TaskStatus status,
            TaskPriority priority,
            ProjectEntity projectEntity,
            UserEntity assignee
    ) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(id);
        taskEntity.setTitle(title);
        taskEntity.setDescription("Task description");
        taskEntity.setStatus(status);
        taskEntity.setPriority(priority);
        taskEntity.setDueDate(LocalDate.now().plusDays(7));
        taskEntity.setProject(projectEntity);
        taskEntity.setAssignee(assignee);
        taskEntity.setCreatedAt(LocalDateTime.now());
        taskEntity.setUpdatedAt(LocalDateTime.now());
        return taskEntity;
    }

    @Test
    void getMyTasks_shouldReturnTasksOfCurrentUser() {
        org.example.tuan3.security.CustomUserDetails currentUser =
                new org.example.tuan3.security.CustomUserDetails(
                        2,
                        "vana@gmail.com",
                        "123456",
                        true,
                        List.of()
                );

        org.springframework.security.core.Authentication authentication = mock(org.springframework.security.core.Authentication.class);
        org.springframework.security.core.context.SecurityContext securityContext = mock(org.springframework.security.core.context.SecurityContext.class);

        when(authentication.getPrincipal()).thenReturn(currentUser);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        org.springframework.security.core.context.SecurityContextHolder.setContext(securityContext);

        when(taskRepository.findByAssignee_Id(2)).thenReturn(List.of(inProgressTask, doneTask));

        List<TaskResponse> responses = taskService.getMyTasks();

        assertEquals(2, responses.size());
        verify(taskRepository).findByAssignee_Id(2);

        org.springframework.security.core.context.SecurityContextHolder.clearContext();
    }
}

