package mrkto.mvoice.mixins.docs;

import com.google.gson.Gson;
import mchorse.mappet.client.gui.scripts.GuiDocumentationOverlayPanel;
import mchorse.mappet.client.gui.scripts.utils.documentation.DocClass;
import mchorse.mappet.client.gui.scripts.utils.documentation.DocMerger;
import mchorse.mappet.client.gui.scripts.utils.documentation.Docs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.Language;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

@Mixin(value = DocMerger.class, remap = false)
public abstract class documentation {
    @Inject(
            method = "addAddonsDocs",
            at = @At(value = "TAIL"),
            remap = false,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void addDocs(Gson gson, List<Docs> docsList, CallbackInfo ci) {
        InputStream stream = GuiDocumentationOverlayPanel.class.getResourceAsStream("/assets/mvoice/docs.json");
        Scanner scanner = new Scanner(stream, "UTF-8");
        Docs mvoice = gson.fromJson(scanner.useDelimiter("\\A").next(), Docs.class);
        mvoice.source = "Mappet Voice";
        mvoice.classes.forEach(clazz -> {
            clazz.source = mvoice.source;
            clazz.methods.forEach(method -> method.source = mvoice.source);
        });
        docsList.add(mvoice);
    }
}
