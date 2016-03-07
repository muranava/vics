package com.infinityworks.webapp.pdf.renderer;

import com.infinityworks.webapp.rest.dto.Flags;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class FlagsKeyRenderer implements Function<Flags, String> {
    @Override
    public String apply(Flags flags) {
        if (flags == null) {
            return "";
        }
        List<String> sb = new ArrayList<>();
        if (flags.getHasPV() != null) {
            sb.add("[Has PV: " + flags.getHasPV() + "] ");
        }
        if (flags.getWantsPv() != null) {
            sb.add("[Wants PV: " + flags.getWantsPv() + "]\n");
        }
        if (flags.getNeedsLift() != null) {
            sb.add("[Needs Lift: " + flags.getNeedsLift() + "]\n");
        }
        if (flags.getNotCanvassedYet() != null) {
            sb.add("[Canvassed: " + flags.getNotCanvassedYet() + "]\n");
        }
        if (flags.getPoster() != null) {
            sb.add("[Poster: " + flags.getPoster() + "] ");
        }
        if (flags.getLikelihoodFrom() != null) {
            sb.add(String.format("[Likelihood: %s]\n", rangeValue(flags.getLikelihoodFrom(), flags.getLikelihoodTo())));
        }
        if (flags.getIntentionFrom() != null) {
            sb.add(String.format("[Intention: %s]\n", rangeValue(flags.getIntentionFrom(), flags.getIntentionTo())));
        }
        return sb.stream()
                .collect(Collectors.joining(""));
    }

    private String rangeValue(int from, int to) {
        return Objects.equals(from, to)
                ? String.valueOf(from)
                : String.format("%d-%d", from, to);
    }
}
