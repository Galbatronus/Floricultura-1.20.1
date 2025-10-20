package net.galbatronus.floricultura.entity.client;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.galbatronus.floricultura.entity.animations.ModAnimationDefinitions;
import net.galbatronus.floricultura.entity.custom.SamoyedoEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class SamoyedoModel<T extends Entity> extends HierarchicalModel<T> {

	private final ModelPart samoyedo;
	private final ModelPart head;
	private final ModelPart mouth;
	private final ModelPart mane;
	private final ModelPart body;
	private final ModelPart tail;
	private final ModelPart leg1;
	private final ModelPart leg2;
	private final ModelPart leg3;
	private final ModelPart leg4;

	public SamoyedoModel(ModelPart root) {
		this.samoyedo = root.getChild("samoyedo");
		this.head = this.samoyedo.getChild("head");
		this.mouth = this.head.getChild("mouth");
		this.mane = this.samoyedo.getChild("mane");
		this.body = this.samoyedo.getChild("body");
		this.tail = this.samoyedo.getChild("tail");
		this.leg1 = this.samoyedo.getChild("leg1");
		this.leg2 = this.samoyedo.getChild("leg2");
		this.leg3 = this.samoyedo.getChild("leg3");
		this.leg4 = this.samoyedo.getChild("leg4");
	}


	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition samoyedo = partdefinition.addOrReplaceChild("samoyedo", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition head = samoyedo.addOrReplaceChild("head", CubeListBuilder.create().texOffs(32, 20).addBox(-2.0F, -5.0F, -2.0F, 6.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(41, 11).addBox(2.0F, -7.0F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(41, 11).addBox(-2.0F, -7.0F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(36, 0).addBox(-0.5F, -2.02F, -5.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -10.5F, -7.0F));

		PartDefinition mouth = head.addOrReplaceChild("mouth", CubeListBuilder.create().texOffs(36, 5).addBox(-1.5F, 0.48F, -4.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -0.5F, -1.0F));

		PartDefinition mane = samoyedo.addOrReplaceChild("mane", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -3.0F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -10.0F, -3.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition body = samoyedo.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 14).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 9.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, 2.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition tail = samoyedo.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(56, 8).addBox(-1.0F, -4.0F, -2.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(52, 9).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(-0.001F))
				.texOffs(48, 10).addBox(-3.0F, -2.0F, -2.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(-0.002F)), PartPose.offset(0.0F, -12.0F, 10.0F));

		PartDefinition leg1 = samoyedo.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(56, 0).addBox(0.0F, 2.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, -8.0F, 7.0F));

		PartDefinition leg2 = samoyedo.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(56, 0).addBox(0.0F, 2.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -8.0F, 7.0F));

		PartDefinition leg3 = samoyedo.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(56, 0).addBox(0.0F, 2.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, -8.0F, -4.0F));

		PartDefinition leg4 = samoyedo.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(56, 0).addBox(0.0F, 2.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -8.0F, -4.0F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}


	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);

		this.animateWalk(ModAnimationDefinitions.SAMOYEDO_WALK, limbSwing, limbSwingAmount, 2f, 2.5f);
		this.animate(((SamoyedoEntity) entity).idleAnimationState, ModAnimationDefinitions.SAMOYEDO_IDLE, ageInTicks,1f);
		this.animate(((SamoyedoEntity) entity).attackAnimationState, ModAnimationDefinitions.SAMOYEDO_ATTACK, ageInTicks,1f);
	}

	private void applyHeadRotation(float pNetHeadYaw, float pHeadPitch, float pAgeInTicks) {
		pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30.0F, 30.0F);
		pHeadPitch = Mth.clamp(pHeadPitch, -25.0F, 45.0F);

		this.head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
		this.head.xRot = pHeadPitch * ((float)Math.PI / 180F);

	}


	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		samoyedo.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return samoyedo;
	}


}