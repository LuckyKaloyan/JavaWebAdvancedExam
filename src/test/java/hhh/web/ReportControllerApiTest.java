package hhh.web;

import hhh.report.model.ReportLocation;
import hhh.report.model.ReportType;
import hhh.report.service.ReportService;
import hhh.security.AuthenticationDetails;
import hhh.user.model.User;
import hhh.user.model.UserRole;
import hhh.user.service.UserService;
import hhh.web.dto.ReportRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.UUID;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReportController.class)
public class ReportControllerApiTest {

    @MockitoBean
    private ReportService reportService;

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private final UUID testUserId = UUID.randomUUID();
    private final User testUser = createTestUser();

    private User createTestUser() {
        return User.builder()
                .id(testUserId)
                .username("testuser")
                .role(UserRole.USER)
                .build();
    }

    private ReportRequest createValidReportRequest() {
        return ReportRequest.builder()
                .troublemaker("John Doe")
                .reportType(ReportType.FRAUD)
                .whereItHappened(ReportLocation.COMMENT)
                .description("Physical altercation during lunch")
                .dateOfIssue(LocalDate.now())
                .build();
    }

    @Test
    @WithAnonymousUser
    void getReportsUnauthenticatedRedirectToLogin() throws Exception {
        mockMvc.perform(get("/reports/my_reports"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser
    void getReportsAuthenticatedReturnMyReportsView() throws Exception {
        when(userService.getById(testUserId)).thenReturn(testUser);

        mockMvc.perform(get("/reports/my_reports")
                        .with(user(new AuthenticationDetails(testUserId, "testuser", "password", UserRole.USER))))
                .andExpect(status().isOk())
                .andExpect(view().name("my_reports"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @WithAnonymousUser
    void getNewReportUnauthenticatedRedirectToLogin() throws Exception {
        mockMvc.perform(get("/reports/create_new_report"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser
    void getNewReportAuthenticatedReturnCreateReportView() throws Exception {
        mockMvc.perform(get("/reports/create_new_report")
                        .with(user(new AuthenticationDetails(testUserId, "testuser", "password", UserRole.USER))))
                .andExpect(status().isOk())
                .andExpect(view().name("create_new_report"))
                .andExpect(model().attributeExists("reportRequest"));
    }

    @Test
    @WithMockUser
    void postNewReportWithValidDataCreateReportAndRedirect() throws Exception {
        ReportRequest validRequest = createValidReportRequest();

        mockMvc.perform(post("/reports/create_new_report")
                        .param("troublemaker", validRequest.getTroublemaker())
                        .param("reportType", validRequest.getReportType().toString())
                        .param("whereItHappened", validRequest.getWhereItHappened().toString())
                        .param("description", validRequest.getDescription())
                        .param("dateOfIssue", validRequest.getDateOfIssue().toString())
                        .with(user(new AuthenticationDetails(testUserId, "testuser", "password", UserRole.USER)))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

    }

    @Test
    @WithMockUser
    void postNewReportWithInvalidDataReturnCreateReportViewWithErrors() throws Exception {
        mockMvc.perform(post("/reports/create_new_report")
                        .param("reportType", "31")
                        .param("whereItHappened", "13")
                        .param("dateOfIssue", "1")
                        .with(user(new AuthenticationDetails(testUserId, "testuser", "password", UserRole.USER)))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("create_new_report"))
                .andExpect(model().attributeHasFieldErrors("reportRequest",
                        "troublemaker", "reportType", "whereItHappened", "description", "dateOfIssue"));
    }

    @Test
    @WithMockUser
    void postNewReportWithFutureDateReturnCreateReportViewWithError() throws Exception {
        mockMvc.perform(post("/reports/create_new_report")
                        .param("troublemaker", "John Doe")
                        .param("reportType", "VIOLENCE")
                        .param("whereItHappened", "CAFETERIA")
                        .param("description", "Test description")
                        .param("dateOfIssue", LocalDate.now().plusDays(1).toString())
                        .with(user(new AuthenticationDetails(testUserId, "testuser", "password", UserRole.USER)))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("create_new_report"))
                .andExpect(model().attributeHasFieldErrors("reportRequest", "dateOfIssue"));
    }
}