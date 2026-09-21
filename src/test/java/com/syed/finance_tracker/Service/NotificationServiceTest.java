import com.syed.finance_tracker.Dto.NotificationResponse;
import com.syed.finance_tracker.entity.Notification;
import com.syed.finance_tracker.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.syed.finance_tracker.Repository.NotificationRepository;
import com.syed.finance_tracker.Repository.UserRepository;
import com.syed.finance_tracker.Service.NotificationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)


class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NotificationService notificationService;


    @Test
    void getNotifications_shouldReturnNotifications() {

        String email = "user@example.com";

        User user = new User(
                "Test User",
                email,
                "hashed-password"
        );
        user.setId(1L);

        Notification notification = new Notification();
        notification.setId(7L);
        notification.setMessage("Your Food budget has been exceeded.");
        notification.setRead(false);
        notification.setUser(user);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication())
                .thenReturn(authentication);

        when(authentication.getName())
                .thenReturn(email);

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(notificationRepository
                .findByUser_IdOrderByCreatedAtDesc(1L))
                .thenReturn(List.of(notification));

        List<NotificationResponse> result =
                notificationService.getNotifications();

        assertEquals(1, result.size());

        assertEquals(
                7L,
                result.get(0).getId()
        );

        assertEquals(
                "Your Food budget has been exceeded.",
                result.get(0).getMessage()
        );

        assertFalse(result.get(0).isRead());

        verify(notificationRepository)
                .findByUser_IdOrderByCreatedAtDesc(1L);
    }

    @Test
    void markAsRead_shouldMarkNotificationAsRead() {

        String email = "user@example.com";

        User user = new User(
                "Test User",
                email,
                "hashed-password"
        );
        user.setId(1L);

        Notification notification = new Notification();
        notification.setId(7L);
        notification.setMessage("Your Food budget has been exceeded.");
        notification.setRead(false);
        notification.setUser(user);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication())
                .thenReturn(authentication);

        when(authentication.getName())
                .thenReturn(email);

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(notificationRepository.findById(7L))
                .thenReturn(Optional.of(notification));

        notificationService.markAsRead(7L);

        assertTrue(notification.isRead());

        verify(notificationRepository)
                .save(notification);
    }
}
