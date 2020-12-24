package com.hackerda.platform.domain.grade;

import com.hackerda.platform.domain.student.StudentUserBO;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * 成绩抓取任务
 */
@Getter
@ToString
public class GradeFetchTask {

    private final boolean tigerByUser;
    private final StudentUserBO tigerStudent;

    public GradeFetchTask(boolean tigerByUser, StudentUserBO tigerStudent) {
        this.tigerByUser = tigerByUser;
        this.tigerStudent = tigerStudent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        GradeFetchTask that = (GradeFetchTask) o;

        return new EqualsBuilder()
                .append(tigerStudent.getUrpClassNum(), that.tigerStudent.getUrpClassNum())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(tigerStudent.getUrpClassNum())
                .toHashCode();
    }
}
