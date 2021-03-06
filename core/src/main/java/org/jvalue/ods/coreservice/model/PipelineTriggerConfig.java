package org.jvalue.ods.coreservice.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Embeddable
public class PipelineTriggerConfig {

    private boolean periodic;

    //time of first execution
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", locale = "UTC")
    private Date firstExecution;

    //execution interval in ms
    private Long interval;


    //Constructor for JPA
    private PipelineTriggerConfig() {
    }

    @JsonCreator
    public PipelineTriggerConfig(
            @JsonProperty("periodic") boolean periodic,
            @JsonProperty("firstExecution") Date firstExecution,
            @JsonProperty("interval") Long interval) {
        this.periodic = periodic;
        this.firstExecution = firstExecution;
        this.interval = interval;
    }

    @Override
    public String toString() {
        return "PipelineTriggerConfig{" +
                "periodic=" + periodic +
                ", firstExecution=" + firstExecution +
                ", interval=" + interval +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PipelineTriggerConfig that = (PipelineTriggerConfig) o;
        return periodic == that.periodic &&
                interval.equals(that.interval) &&
                firstExecution.equals(that.firstExecution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(periodic, firstExecution, interval);
    }

    public boolean isPeriodic() {
        return periodic;
    }

    public Date getFirstExecution() {
        return firstExecution;
    }

    public Long getInterval() {
        return interval;
    }
}
