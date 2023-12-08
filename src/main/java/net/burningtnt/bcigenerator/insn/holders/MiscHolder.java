package net.burningtnt.bcigenerator.insn.holders;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.burningtnt.bcigenerator.insn.Parser;

import java.util.List;

public final class MiscHolder {
    private MiscHolder() {
    }

    public static List<LiteralArgumentBuilder<Parser.InsnReference>> init() {
        return List.of(
        );
    }
}
