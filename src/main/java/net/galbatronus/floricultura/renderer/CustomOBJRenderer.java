package net.galbatronus.floricultura.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import de.javagl.obj.Obj;
import de.javagl.obj.ObjReader;
import de.javagl.obj.ObjFace;
import de.javagl.obj.FloatTuple;

import java.io.InputStream;
import java.io.IOException;
import java.util.Optional;

public class CustomOBJRenderer {
    private Obj model;
    private ResourceLocation texture;

    public void loadModel(String objPath, String texturePath) {
        try {
            Optional<Resource> resourceOptional = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(objPath));

            if (resourceOptional.isPresent()) {
                try (InputStream objInputStream = resourceOptional.get().open()) {
                    model = ObjReader.read(objInputStream);
                }
            } else {
                throw new IOException("Resource not found: " + objPath);
            }

            texture = new ResourceLocation(texturePath);

        } catch (Exception e) {
            System.err.println("Error loading OBJ model: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void renderPart(String partName, PoseStack poseStack, MultiBufferSource buffer) {
        if (model == null || texture == null) {
            System.err.println("Model or texture not loaded");
            return;
        }

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entitySolid(texture));

        for (int i = 0; i < model.getNumFaces(); i++) {
            ObjFace face = model.getFace(i);

            renderFace(face, poseStack, vertexConsumer);
        }
    }

    private void renderFace(ObjFace face, PoseStack poseStack, VertexConsumer vertexConsumer) {
        poseStack.pushPose();

        for (int i = 0; i < face.getNumVertices(); i++) {
            int vertexIndex = face.getVertexIndex(i);
            FloatTuple vertex = model.getVertex(vertexIndex);


            float u = 0, v = 0;
            if (model.getNumTexCoords() > 0) {
                int texCoordIndex = face.getTexCoordIndex(i);
                if (texCoordIndex >= 0) {
                    FloatTuple texCoord = model.getTexCoord(texCoordIndex);
                    u = texCoord.get(0);
                    v = texCoord.get(1);
                }
            }

            vertexConsumer.vertex(poseStack.last().pose(),
                            vertex.get(0), vertex.get(1), vertex.get(2))
                    .color(255, 255, 255, 255)
                    .uv(u, v)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(15728880)  // Luz máxima
                    .normal(1, 0, 0)
                    .endVertex();
        }

        poseStack.popPose();
    }

    public void render(PoseStack poseStack, MultiBufferSource buffer) {
        renderPart(null, poseStack, buffer);
    }
}