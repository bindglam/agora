package com.bindglam.agora.user;

import com.bindglam.agora.board.Board;
import com.bindglam.agora.post.Post;
import com.bindglam.agora.post.PostVisibility;
import org.jspecify.annotations.NonNull;

public final class PermissionUtil {
    private PermissionUtil() {}

    public static boolean canViewPost(@NonNull User user, @NonNull Post post) {
        PostVisibility visibility = post.getVisibility();
        UserPermission permission = user.getPermission();

        return switch (visibility) {
            case PUBLIC, NOT_LISTED -> true;
            case PRIVATE -> permission.check(UserPermission.ADMIN);
        };
    }

    public static boolean canPostOnBoard(@NonNull User user, @NonNull Board board) {
        UserPermission userPermission = user.getPermission();
        UserPermission requiredPermission = board.properties().getPermissions().getPosting();
        return userPermission.check(requiredPermission);
    }

    public static boolean canCommentOnPost(@NonNull User user, @NonNull Post post) {
        UserPermission userPermission = user.getPermission();
        UserPermission requiredPermission = post.getBoard().properties().getPermissions().getComment();
        return userPermission.check(requiredPermission);
    }
}
