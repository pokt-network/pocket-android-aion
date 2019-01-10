package network.pokt.aion.rpc.types;

import java.math.BigInteger;

public class BlockTag {

    private BigInteger blockHeight;
    private DefaultBlock defaultBlock;

    public enum DefaultBlock {
        EARLIEST("earliest"),LATEST("latest");

        private String tagString;

        DefaultBlock(String tagString) {
            this.tagString = tagString;
        }

        public String getTagString() {
            return tagString;
        }
    }

    public BlockTag(BigInteger blockHeight) {
        this.blockHeight = blockHeight;
    }

    public BlockTag(DefaultBlock defaultBlock) {
        this.defaultBlock = defaultBlock;
    }

    public String getBlockTagString() {
        if(this.blockHeight != null) {
            return this.blockHeight.toString(16);
        } else if(this.defaultBlock != null) {
            return this.defaultBlock.getTagString();
        }
        return null;
    }

    public static BlockTag tagOrLatest(BlockTag blockTag) {
        return blockTag == null ? new BlockTag(DefaultBlock.LATEST) : blockTag;
    }
}