package com.urbanrides.model;


import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "get_support_logs")

public class SupportTypeLogs {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "support_logs_id")
    private int supportLogsId;

    @Column(name = "support_type")
    private String supportType;

    @NotBlank(message = "Description is required")
    @Size(max = 150, message = "Description msg must be less than 150 characters")
    private String description;

    @Column(name = "is_solved")
    private boolean isSolved = false;

    @Column(name = "is_file")
    private boolean isFile;

    @Column(name = "file_name")
    private String fileName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User userObj;

    @Column(name = "support_case_id")
    private String supportCaseId;
    @Column(name = "file_extention")
    private String fileExtention;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "admin_id")
    private User adminObj;
    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDate createdDate;
}
