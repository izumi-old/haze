package org.izumi.haze.modules.impl.java.util.impl;

import org.izumi.haze.modules.impl.java.source.Comment;
import org.izumi.haze.modules.impl.java.util.Comments;
import org.izumi.haze.modules.impl.java.util.Elements;

import java.util.Collection;
import java.util.LinkedList;

public class CommentsImpl extends LinkedList<Comment> implements Comments {
    public CommentsImpl() {
    }

    public CommentsImpl(Collection<? extends Comment> c) {
        super(c);
    }

    @Override
    public Elements<Comment> getOrderedCopy() {
        CommentsImpl comments = new CommentsImpl(this);
        comments.sort(new ElementComparator());
        return comments;
    }
}
