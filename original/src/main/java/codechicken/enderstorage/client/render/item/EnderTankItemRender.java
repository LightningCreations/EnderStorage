package codechicken.enderstorage.client.render.item;

import codechicken.enderstorage.api.Frequency;
import codechicken.enderstorage.client.render.tile.RenderTileEnderTank;
import codechicken.enderstorage.network.TankSynchroniser;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.RenderUtils;
import codechicken.lib.render.item.IItemRenderer;
import codechicken.lib.util.TransformUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by covers1624 on 4/27/2016.
 */
public class EnderTankItemRender implements IItemRenderer {

    @Override
    public void renderItem(ItemStack item, TransformType transformType) {
        GlStateManager.pushMatrix();
        CCRenderState ccrs = CCRenderState.instance();
        ccrs.reset();
        ccrs.pullLightmap();
        Frequency frequency = Frequency.readFromStack(item);
        FluidStack fluidStack = TankSynchroniser.getClientLiquid(frequency);
        RenderTileEnderTank.renderTank(ccrs, 2, 0F, frequency, 0, 0, 0, 0);
        if (fluidStack != null && RenderUtils.shouldRenderFluid(fluidStack)) {
            RenderTileEnderTank.renderLiquid(fluidStack, 0, 0, 0);
        }
        //Fixes issues with inventory rendering.
        //The Portal renderer modifies blend and disables it.
        //Vanillas inventory relies on the fact that items don't modify gl so it never bothers to set it again.
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.popMatrix();
    }

    @Override
    public IModelState getTransforms() {
        return TransformUtils.DEFAULT_BLOCK;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }
}
