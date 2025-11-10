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
    PROG_LANG,
    DB,
    DEVOPS,
    FRONTEND,
    TESTING,
    DATA,
    FRAMEWORK;

}
