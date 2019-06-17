package com.example.hawkish.arschedule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;

import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.Pose;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;

import java.util.concurrent.CompletableFuture;

public class MyArNod extends AnchorNode {
    private AugmentedImage image;
    private static CompletableFuture<ModelRenderable> modelRenderableCompletableFuture;

    @SuppressLint("NewApi")
    public MyArNod(Context context, int modelId){
        if(modelRenderableCompletableFuture==null){
            modelRenderableCompletableFuture = ModelRenderable.builder()
                    .setRegistryId("my_model")
                    .setSource(context,modelId)
                    .build();
        }

    }

    public AugmentedImage getImage() {
        return image;
    }

    public void setImage(AugmentedImage image) {
        this.image = image;
        if(!modelRenderableCompletableFuture.isDone()){
            CompletableFuture.allOf(modelRenderableCompletableFuture)
                    .thenAccept((Void avoid)-> {
                       setImage(image);
                    }).exceptionally(throwable->{return null;});
        }
        setAnchor(image.createAnchor(image.getCenterPose()));
        Node node = new Node();
        Pose pose = Pose.makeTranslation(0.0f,0.0f,0.25f);
        node.setParent(this);
        node.setLocalPosition(new Vector3(pose.tx(),pose.ty(),pose.tz()));
        node.setLocalRotation(new Quaternion(pose.qx(),pose.qy(),pose.qz(),pose.qw()));
        node.setRenderable(modelRenderableCompletableFuture.getNow(null));

    }



}
