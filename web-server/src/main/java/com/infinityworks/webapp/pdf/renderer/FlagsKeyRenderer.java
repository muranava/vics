package com.infinityworks.webapp.pdf.renderer;

import com.infinityworks.webapp.rest.dto.Flags;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Function;

@Component
public class FlagsKeyRenderer implements Function<Flags, String> {
    @Override
    public String apply(Flags flags) {
        if (flags == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if (flags.getHasPV() != null) {
            sb.append("[Has PV: ").append(flags.getHasPV()).append("] ");
        }
        if (flags.getWantsPv() != null) {
            sb.append("[Wants PV: ").append(flags.getWantsPv()).append("]\n");
        }
        if (flags.getNeedsLift() != null) {
            sb.append("[Needs Lift: ").append(flags.getNeedsLift()).append("]\n");
        }
        if (flags.getNotCanvassedYet() != null) {
            sb.append("[Canvassed: ").append(flags.getNotCanvassedYet()).append("]\n");
        }
        if (flags.getPoster() != null) {
            sb.append("[Poster: ").append(flags.getPoster()).append("] ");
        }
        if (flags.getLikelihoodFrom() != null) {
            sb.append(String.format("[Likelihood: %s]\n", rangeValue(flags.getLikelihoodFrom(), flags.getLikelihoodTo())));
        }
        if (flags.getIntentionFrom() != null) {
            sb.append(String.format("[Intention: %s]\n", rangeValue(flags.getIntentionFrom(), flags.getIntentionTo())));
        }
        return sb.toString();
    }

    private String rangeValue(int from, int to) {
        return Objects.equals(from, to)
                ? String.valueOf(from)
                : String.format("%d-%d", from, to);
    }
}
