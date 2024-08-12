package com.urbanrides.model;


import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notifications_logs")

public class NotificationLogs {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int notificationId;

    @NotBlank(message = "Notification type is required")
    @Size(max = 30, message = "Notification type must be less than 30 characters")
    @Column(name = "notification_Type")
    private String notificationType;

    @Column(name = "created_date")
    @CreationTimestamp
    private LocalDateTime createdDate;

    @NotBlank(message = "Notification msg type is required")
    @Size(max = 256, message = "Notification msg must be less than 256 characters")
    @Column(name = "notification_msg")
    private String notificationMsg;

    @NotNull(message = "user id is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
