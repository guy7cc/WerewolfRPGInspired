package io.github.guy7cc.wwrpg.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.guy7cc.wwrpg.WerewolfRPGInspired;
import io.github.guy7cc.wwrpg.core.GameType;
import io.github.guy7cc.wwrpg.core.PlayerPreferences;
import io.github.guy7cc.wwrpg.core.werewolf.Role;
import io.github.guy7cc.wwrpg.core.werewolf.RoleSettings;
import io.github.guy7cc.wwrpg.core.werewolf.WerewolfGameSettings;
import io.github.guy7cc.wwrpg.network.PacketManager;
import io.github.guy7cc.wwrpg.network.ServerboundSaveSettingsPacket;
import io.github.guy7cc.wwrpg.network.ServerboundStartWerewolfGamePacket;
import io.github.guy7cc.wwrpg.util.IntRange;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

public class WerewolfGameScreen extends Screen {
    public static final ResourceLocation INVENTORY_BUTTON_LOCATION = new ResourceLocation(WerewolfRPGInspired.ID, "textures/gui/werewolf_game_button.png");
    public static final ResourceLocation PICTURE_LOCATION = new ResourceLocation(WerewolfRPGInspired.ID, "textures/gui/werewolf_game.png");

    private Rect gameTypeRect;
    private Rect prefRect;
    private Rect settingsRect;
    private Rect rolesRect;
    private Rect spawnPosRect;
    private Rect dayTimeRect;
    private Rect nightTimeRect;

    private Button gameTypeLeftButton;
    private Button gameTypeRightButton;
    private Button prefEnabledButton;
    private Button desiredRoleLeftButton;
    private Button desiredRoleRightButton;
    private Button saveButton;
    private Button startButton;

    private boolean prefEnabled = false;
    private int desiredRole = 0;
    private Map<Role, EditBox> roleEdits = new HashMap<>();
    private EditBox spawnPosEdit;
    private EditBox dayTimeEdit;
    private EditBox nightTimeEdit;

    private boolean forceStart = false;

    private Component errorMessage = Component.literal("");

    public WerewolfGameScreen() {
        super(Component.translatable("wwrpg.gui.werewolfGameScreen"));
    }

    @Override
    protected void init() {
        gameTypeRect = new Rect(width / 2 - 163, height / 2 - 110, 125, 28);
        prefRect = new Rect(width / 2 - 163, height / 2 - 82, 125, 192);
        settingsRect = new Rect(width / 2 - 38, height / 2 - 110, 200, 220);
        rolesRect = new Rect(settingsRect.x + 20, settingsRect.y + 35, 68, 132);
        spawnPosRect = new Rect(settingsRect.x + 112, settingsRect.y + 35, 68, 20);
        dayTimeRect = new Rect(settingsRect.x + 112, spawnPosRect.y + 40, 68, 20);
        nightTimeRect = new Rect(settingsRect.x + 112, dayTimeRect.y + 40, 68, 20);

        if(gameTypeLeftButton == null){
            gameTypeLeftButton = new Button.Builder(Component.literal("<"), button -> {}).pos(gameTypeRect.x + 6, gameTypeRect.y + 6).size(8, 16).build();
        } else {
            gameTypeLeftButton.setPosition(gameTypeRect.x + 6, gameTypeRect.y + 6);
        }
        if(prefEnabledButton == null){
            prefEnabledButton = new Button.Builder(Component.translatable("wwrpg.gui." + (prefEnabled ? "enabled" : "disabled")), button -> {
                prefEnabled = !prefEnabled;
                button.setMessage(Component.translatable("wwrpg.gui." + (prefEnabled ? "enabled" : "disabled")));
            }).pos(prefRect.midX() - 40, prefRect.y + 25).size(80, 20).build();
        } else {
            prefEnabledButton.setPosition(prefRect.midX() - 40, prefRect.y + 35);
        }
        if(desiredRoleLeftButton == null){
            desiredRoleLeftButton = new Button.Builder(Component.literal("<"), button -> {
                desiredRole++;
                desiredRole %= Role.values().length;
            }).pos(prefRect.midX() - )
        }
        if(gameTypeRightButton == null){
            gameTypeRightButton = new Button.Builder(Component.literal(">"), button -> {
                // change screen
            }).pos(gameTypeRect.right() - 14, gameTypeRect.y + 6).size(8, 16).build();
        } else {
            gameTypeRightButton.setPosition(gameTypeRect.right() - 14, gameTypeRect.y + 6);
        }
        if(spawnPosEdit == null){
            spawnPosEdit = new EditBox(minecraft.font, spawnPosRect.x, spawnPosRect.y, spawnPosRect.w, spawnPosRect.h, Component.literal(""));
            spawnPosEdit.setResponder(str -> refresh());
        } else {
            spawnPosEdit.setPosition(spawnPosRect.x, spawnPosRect.y);
        }
        if(dayTimeEdit == null){
            dayTimeEdit = new EditBox(minecraft.font, dayTimeRect.x, dayTimeRect.y, dayTimeRect.w, dayTimeRect.h, Component.literal(""));
            dayTimeEdit.setResponder(str -> refresh());
        } else {
            dayTimeEdit.setPosition(dayTimeRect.x, dayTimeRect.y);
        }
        if(nightTimeEdit == null){
            nightTimeEdit = new EditBox(minecraft.font, nightTimeRect.x, nightTimeRect.y, nightTimeRect.w, nightTimeRect.h, Component.literal(""));
            nightTimeEdit.setResponder(str -> refresh());
        } else {
            nightTimeEdit.setPosition(nightTimeRect.x, nightTimeRect.y);
        }
        if(saveButton == null){
            saveButton = new Button.Builder(Component.translatable("wwrpg.gui.save"), button -> {
                save();
            }).pos(settingsRect.midX() - 83, settingsRect.bottom() - 26).size(80, 20).build();
        } else {
            saveButton.setPosition(settingsRect.midX() - 83, settingsRect.bottom() - 26);
        }
        if(startButton == null){
            startButton = new Button.Builder(Component.translatable("wwrpg.gui.start"), button -> {
                WerewolfGameSettings settings = refresh();
                if(settings != null){
                    PacketManager.sendToServer(new ServerboundStartWerewolfGamePacket(settings));
                    onClose();
                }
            }).pos(settingsRect.midX() + 3, settingsRect.bottom() - 26).size(80, 20).build();
        } else {
            startButton.setPosition(settingsRect.midX() + 3, settingsRect.bottom() - 26);
        }

        gameTypeLeftButton.active = false;
        gameTypeRightButton.active = false;

        addRenderableWidget(gameTypeLeftButton);
        addRenderableWidget(gameTypeRightButton);
        addRenderableWidget(prefEnabledButton);
        addRenderableWidget(spawnPosEdit);
        addRenderableWidget(dayTimeEdit);
        addRenderableWidget(nightTimeEdit);
        addRenderableWidget(saveButton);
        addRenderableWidget(startButton);

        int h = rolesRect.y;
        if(roleEdits.isEmpty()){
            for(Role role : Role.values()){
                if(role == Role.VILLAGER) continue;
                EditBox edit = new EditBox(minecraft.font, rolesRect.x + 22, h, 50, 20, Component.literal(""));
                edit.setResponder(str -> refresh());
                roleEdits.put(role, edit);
                addRenderableWidget(edit);
                h += 22;
            }
        } else {
            for(Role role : Role.values()){
                if(role == Role.VILLAGER) continue;
                EditBox edit = roleEdits.get(role);
                edit.setPosition(rolesRect.x + 22, h);
                addRenderableWidget(edit);
                h += 22;
            }
        }
        refresh();
    }

    @Override
    public void tick() {
        for(EditBox edit : roleEdits.values()){
            edit.tick();
        }
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pPoseStack);
        renderBox(pPoseStack, gameTypeRect);
        renderBox(pPoseStack, prefRect);
        renderBox(pPoseStack, settingsRect);
        drawCenteredStringNoShadow(pPoseStack, minecraft.font, Component.translatable("wwrpg.gui.preferences"), prefRect.midX(), prefRect.y + 6, 0x404040);
        drawCenteredStringNoShadow(pPoseStack, minecraft.font, Component.translatable("wwrpg.gui.settings"), settingsRect.midX(), settingsRect.y + 6, 0x404040);
        drawCenteredStringNoShadow(pPoseStack, minecraft.font, Component.translatable("wwrpg.gui.spawnPos"), spawnPosRect.midX(), spawnPosRect.y - 12, 0x404040);
        drawCenteredStringNoShadow(pPoseStack, minecraft.font, Component.translatable("wwrpg.gui.dayTime"), dayTimeRect.midX(), dayTimeRect.y - 12, 0x404040);
        drawCenteredStringNoShadow(pPoseStack, minecraft.font, Component.translatable("wwrpg.gui.nightTime"), nightTimeRect.midX(), nightTimeRect.y - 12, 0x404040);
        drawCenteredStringNoShadow(pPoseStack, minecraft.font, errorMessage, settingsRect.midX(), settingsRect.bottom() - 40, ChatFormatting.RED.getColor());
        renderGameType(pPoseStack);
        renderRoles(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    private void renderBox(PoseStack poseStack, Rect rect){
        final int x = rect.x;
        final int y = rect.y;
        final int w = rect.w;
        final int h = rect.h;
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderTexture(0, PICTURE_LOCATION);
        blit(poseStack, x, y, 0, 0, 4, 4, 128, 128);
        blit(poseStack, x + 4, y, w - 8, 4, 4, 0, 8, 4, 128, 128);
        blit(poseStack, x + w - 4, y, 12, 0, 4, 4, 128, 128);
        blit(poseStack, x, y + 4, 4, h - 8, 0, 4, 4, 8, 128, 128);
        blit(poseStack, x + 4, y + 4, w - 8, h - 8, 4, 4, 8, 8, 128, 128);
        blit(poseStack, x + w - 4, y + 4, 4, h - 8, 12, 4, 4, 8, 128, 128);
        blit(poseStack, x, y + h - 4, 0, 12, 4, 4, 128, 128);
        blit(poseStack, x + 4, y + h - 4, w - 8, 4, 4, 12, 8, 4, 128, 128);
        blit(poseStack, x + w - 4, y + h - 4, 12, 12, 4, 4, 128, 128);
    }

    public static void drawCenteredStringNoShadow(PoseStack pPoseStack, Font pFont, Component pText, int pX, int pY, int pColor) {
        pFont.draw(pPoseStack, pText, (float)(pX - pFont.width(pText) / 2), (float)pY, pColor);
    }

    private void renderGameType(PoseStack poseStack){
        fill(poseStack, gameTypeRect.x + 18, gameTypeRect.y + 4, gameTypeRect.right() - 18, gameTypeRect.bottom() - 4, 0xFF5F5F60);
        fill(poseStack, gameTypeRect.x + 19, gameTypeRect.y + 5, gameTypeRect.right() - 19, gameTypeRect.bottom() - 5, 0xFF000000);
        drawCenteredStringNoShadow(poseStack, minecraft.font, GameType.WEREWOLF.getComponent(), gameTypeRect.midX(), gameTypeRect.y + 10, 0xFFFFFF);
    }

    private void renderRoles(PoseStack poseStack){
        drawCenteredStringNoShadow(poseStack, minecraft.font, Component.translatable("wwrpg.gui.roles"), rolesRect.midX(), rolesRect.y - 12, 0x404040);
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderTexture(0, PICTURE_LOCATION);
        int i = 1;
        for(Role role : Role.values()){
            if(role == Role.VILLAGER) continue;
            fill(poseStack, rolesRect.x, rolesRect.y - 1 + (i - 1) * 22, rolesRect.x + 22, rolesRect.y - 1 + i * 22, -6250336);
            fill(poseStack, rolesRect.x + 1, rolesRect.y + (i - 1) * 22, rolesRect.x + 21, rolesRect.y - 2 + i * 22, -16777216);
            blit(poseStack, rolesRect.x + 2, rolesRect.y + i * 22 - 20, 16, i * 16, 18, 16, 128, 128);
            i++;
        }
    }

    public void applySettings(WerewolfGameSettings settings){
        RoleSettings roleSettings = settings.get("Roles");
        Map<Role, IntRange> ranges = roleSettings.get();
        for(Role role : Role.values()){
            if(role == Role.VILLAGER) continue;
            roleEdits.get(role).setValue(ranges.get(role).toString());
        }
        Vec3 spawnPos = settings.get("SpawnPos");
        spawnPosEdit.setValue(spawnPos.x + " " + spawnPos.y + " " + spawnPos.z);
        dayTimeEdit.setValue(String.valueOf(settings.<Integer>get("DayTime")));
        nightTimeEdit.setValue(String.valueOf(settings.<Integer>get("NightTime")));
        forceStart = settings.get("ForceStart");
        refresh();
    }

    public void applyPreferences(PlayerPreferences preferences){
        prefEnabled = preferences.isEnabled();
        prefEnabledButton.setMessage(Component.translatable("wwrpg.gui." + (prefEnabled ? "enabled" : "disabled")));
        desiredRole = preferences.getDesiredRole().ordinal();
    }

    private WerewolfGameSettings refresh(){
        boolean settingsCreatable = true;
        Map<Role, IntRange> ranges = new HashMap<>();
        Vec3 spawnPos = null;
        int dayTime = 1;
        int nightTime = 1;
        for(Map.Entry<Role, EditBox> entry : roleEdits.entrySet()){
            Role role = entry.getKey();
            EditBox edit = entry.getValue();
            try{
                IntRange range = IntRange.fromString(edit.getValue());
                ranges.put(role, range);
                edit.setTextColor(0xFFFFFF);
            } catch(Exception exception){
                settingsCreatable = false;
                edit.setTextColor(ChatFormatting.RED.getColor());
            }
        }
        try{
            String[] str = spawnPosEdit.getValue().split(" ");
            spawnPos = new Vec3(Double.parseDouble(str[0]), Double.parseDouble(str[1]), Double.parseDouble(str[2]));
            spawnPosEdit.setTextColor(0xFFFFFF);
        } catch(Exception exception){
            settingsCreatable = false;
            spawnPosEdit.setTextColor(ChatFormatting.RED.getColor());
        }
        try{
            dayTime = Integer.parseInt(dayTimeEdit.getValue());
            if(dayTime <= 0) throw new IllegalArgumentException();
            dayTimeEdit.setTextColor(0xFFFFFF);
        } catch(Exception exception){
            settingsCreatable = false;
            dayTimeEdit.setTextColor(ChatFormatting.RED.getColor());
        }
        try{
            nightTime = Integer.parseInt(nightTimeEdit.getValue());
            if(nightTime <= 0) throw new IllegalArgumentException();
            nightTimeEdit.setTextColor(0xFFFFFF);
        } catch(Exception exception){
            settingsCreatable = false;
            nightTimeEdit.setTextColor(ChatFormatting.RED.getColor());
        }
        WerewolfGameSettings settings = null;
        if(settingsCreatable){
            RoleSettings roleSettings = new RoleSettings(ranges);
            settings = new WerewolfGameSettings(roleSettings, spawnPos, dayTime, nightTime);
            if(settings.isValid(null)){
                errorMessage = Component.literal("");
                saveButton.active = true;
                startButton.active = true;
            } else {
                errorMessage = Component.translatable("wwrpg.gui.invalidSettings");
                for(EditBox edit : roleEdits.values()){
                    edit.setTextColor(ChatFormatting.RED.getColor());
                }
                saveButton.active = true;
                startButton.active = forceStart;
            }
        } else {
            errorMessage = Component.translatable("wwrpg.gui.invalidInputFormat");
            saveButton.active = false;
            startButton.active = false;
        }
        return settings;
    }

    private void save(){
        WerewolfGameSettings settings = refresh();
        if(settings != null){
            PacketManager.sendToServer(new ServerboundSaveSettingsPacket(GameType.WEREWOLF, settings));
        }
    }

    public record Rect(int x, int y, int w, int h){
        public int right(){
            return x + w;
        }
        public int bottom(){
            return y + h;
        }
        public int midX(){
            return x + w / 2;
        }
        public int midY(){
            return y + h / 2;
        }
    }
}
