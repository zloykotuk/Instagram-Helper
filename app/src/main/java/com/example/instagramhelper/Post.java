package com.example.instagramhelper;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Post{
    @SerializedName("graphql")
    @Expose
    private GraphQL graphql;

    public Post(GraphQL graphql) {
        this.graphql = graphql;
    }

    public GraphQL getGraphql() {
        return graphql;
    }
}

class GraphQL{
    @SerializedName("user")
    @Expose
    private User user;

    public GraphQL(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}

class User{

    @SerializedName("edge_owner_to_timeline_media")
    @Expose
    private EdgeOwnerToTimelineMedia edge_owner_to_timeline_media;

    public User(EdgeOwnerToTimelineMedia edge_owner_to_timeline_media) {
        this.edge_owner_to_timeline_media = edge_owner_to_timeline_media;
    }

    public EdgeOwnerToTimelineMedia getEdge_owner_to_timeline_media() {
        return edge_owner_to_timeline_media;
    }

}

class EdgeOwnerToTimelineMedia{
    @SerializedName("edges")
    @Expose
    private ArrayList<Edge> edges;

    public EdgeOwnerToTimelineMedia(ArrayList<Edge> edges) {
        this.edges = edges;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }
}

class Edge{
    @SerializedName("node")
    @Expose
    private Node node;

    public Edge(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }
}

class Node{
    @SerializedName("display_url")
    @Expose
    private String display_url;

    public Node(String display_url) {
        this.display_url = display_url;
    }

    public String getDisplay_url() {
        return display_url;
    }
}