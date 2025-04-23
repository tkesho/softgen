package org.test.sotfgen.dto;

public record PostHistorySearchParams(
        Integer postId,
        String title,
        String body,
        Integer ownerId,
        Integer groupId,
        Boolean hidden
) {
}
