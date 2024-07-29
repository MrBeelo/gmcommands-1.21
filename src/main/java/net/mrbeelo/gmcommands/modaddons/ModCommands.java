package net.mrbeelo.gmcommands.modaddons;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;

import java.util.Collection;

public class ModCommands {

    public static void load() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {

            //REGISTERING

            registerCommand(dispatcher, "gmc", GameMode.CREATIVE, "Creative Mode");
            registerCommand(dispatcher, "gms", GameMode.SURVIVAL, "Survival Mode");
            registerCommand(dispatcher, "gma", GameMode.ADVENTURE, "Adventure Mode");
            registerCommand(dispatcher, "gmsp", GameMode.SPECTATOR, "Spectator Mode");
        });
    }

    //REGISTRATION METHOD

    private static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, String name, GameMode gameMode, String modeName) {
        dispatcher.register(
                CommandManager.literal(name)
                        .requires(source -> source.hasPermissionLevel(2)) // Adjust permission level as needed
                        .executes(context -> execute(context, gameMode, modeName)) // Default to the command sender
                        .then(CommandManager.argument("targets", EntityArgumentType.players())
                                .executes(context -> executeOnTargets(context, gameMode, modeName)))
        );
    }

    //STANDALONE EXECUTION

    private static int execute(CommandContext<ServerCommandSource> context, GameMode gameMode, String modeName) {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (player != null) {
            player.changeGameMode(gameMode);
            source.sendFeedback(() -> Text.literal("Set own game mode to " + modeName), false);
        }

        return Command.SINGLE_SUCCESS;
    }

    //EXECUTION WITH TARGET SELECTOR

    private static int executeOnTargets(CommandContext<ServerCommandSource> context, GameMode gameMode, String modeName) {
        try {
            Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "targets");
            for (ServerPlayerEntity player : players) {
                player.changeGameMode(gameMode);
                //player.sendMessage(Text.literal("Game mode switched to " + modeName + "."), false);
            }
            context.getSource().sendFeedback(() -> Text.literal("Set " + players.size() + " players' game modes to " + modeName), false);
            return Command.SINGLE_SUCCESS;
        } catch (CommandSyntaxException e) {
            context.getSource().sendError(Text.literal("Error: " + e.getMessage()));
            return 0;
        }
    }
}
