package lol.khakikukhi.project_3_1_resit.filter;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.regex.Pattern;

@Component
public class NormalisedEndpointResolver implements EndpointResolver{
    private static final Pattern NUMBER = Pattern.compile("\\d+");
    private static final Pattern UUID = Pattern.compile("[0-9a-fA-F-]{32,}");

    private static final Set<String> STATIC_EXTENSIONS = Set.of(
            "jpg","jpeg","png","gif","webp","svg","ico",
            "css","js","map",
            "woff","woff2","ttf",
            "mp4","webm",
            "pdf"
    );

    @Override
    public String resolve(String path) {

        String[] segments = path.split("/");

        if (segments.length == 0) {
            return path;
        }

        String last = segments[segments.length - 1];

        int dotIndex = last.lastIndexOf('.');
        if (dotIndex > 0) {

            String ext = last.substring(dotIndex + 1).toLowerCase();

            if (STATIC_EXTENSIONS.contains(ext)) {

                StringBuilder prefix = new StringBuilder();

                for (int i = 0; i < segments.length - 1; i++) {
                    if (!segments[i].isEmpty()) {
                        prefix.append("/").append(segments[i]);
                    }
                }

                return prefix.append("/*").toString();
            }
        }

        StringBuilder key = new StringBuilder();

        for (String segment : segments) {

            if (segment.isEmpty()) {
                continue;
            }

            if (NUMBER.matcher(segment).matches()
                    || UUID.matcher(segment).matches()
                    || segment.length() > 20) {

                key.append("/*");
            } else {
                key.append("/").append(segment);
            }
        }

        return key.toString();
    }
}


