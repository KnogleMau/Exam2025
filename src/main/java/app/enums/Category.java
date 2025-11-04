package app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
@ToString

public enum Category {
    PROG_LANG(
            "Java, Python, C#, JavaScript",
            "General-purpose programming languages"
    ),
    DB(
            "PostgreSQL, MySQL, MongoDB",
            "Databases and data storage technologies"
    ),
    DEVOPS(
            "Docker, Kubernetes, GitHub Actions",
            "Tools and practices for deployment, CI/CD, and infrastructure"
    ),
    FRONTEND(
            "HTML, CSS, TypeScript, Vue.js",
            "Front-end and UI-related technologies"
    ),
    TESTING(
            "JUnit, Cypress, Jest",
            "Tools and frameworks for testing and QA"
    ),
    DATA(
            "Pandas, TensorFlow, Power BI",
            "Data science, analytics, and machine learning tools"
    ),
    FRAMEWORK(
            "Spring Boot, React, Angular",
            "Application frameworks and libraries"
    );


    private String exampleName;
    private String description;
    Category(String exampleName, String description) {
        this.exampleName = exampleName;
        this.description = description;
    }

    public String getSlugList() {
        return Arrays.stream(exampleName.split(","))
                .map(String::trim)
                .map(name -> name.toLowerCase()
                        .replace(" ", "-")
                        .replace(".", "")
                        .replace("#", "sharp"))
                .collect(Collectors.joining(","));
    }
}
