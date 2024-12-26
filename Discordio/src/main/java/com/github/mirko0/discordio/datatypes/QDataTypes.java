package com.github.mirko0.discordio.datatypes;

import com.github.mirko0.discordio.AddonMain;
import com.github.mirko0.discordio.datatypes.specifications.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataTypeSpecification;

public class QDataTypes {
    private static final String PREFIX = "DISCORDIO_";


    private static final String USER_DATATYPE_NAME = PREFIX + "USER";
    private static final String GUILD_DATATYPE_NAME = PREFIX + "GUILD";
    private static final String MESSAGE_CHANNEL_DATATYPE_NAME = PREFIX + "MESSAGECHANNEL";
    private static final String OFFSET_DATE_TIME_NAME = PREFIX + "OFFSETDATETIME";
    private static final String CHANNEL_TYPE_NAME = PREFIX + "CHANNELTYPE";

    public static void registerDataTypes() {
        registerDataType(USER_DATATYPE_NAME, new DiscordUserSpecification());
        registerDataType(GUILD_DATATYPE_NAME, new GuildSpecification());
        registerDataType(MESSAGE_CHANNEL_DATATYPE_NAME, new MessageChannelSpecification());
        registerDataType(OFFSET_DATE_TIME_NAME, new OffsetDateTimeSpecification());
        registerDataType(CHANNEL_TYPE_NAME, new ChannelTypeSpecification());

        DISCORD_USER = DataType.getCustomDataType(USER_DATATYPE_NAME);
        DISCORD_GUILD = DataType.getCustomDataType(GUILD_DATATYPE_NAME);
        MESSAGE_CHANNEL = DataType.getCustomDataType(MESSAGE_CHANNEL_DATATYPE_NAME);
        OFFSET_DATE_TIME = DataType.getCustomDataType(OFFSET_DATE_TIME_NAME);
        CHANNEL_TYPE = DataType.getCustomDataType(CHANNEL_TYPE_NAME);
    }

    private static void registerDataType(String name, DataTypeSpecification specification) {
        DataType.registerCustomDataType(name, specification);
        AddonMain.log("Data Type Registered: \n" + name + " (" + specification.getClass().getName() + ")\n"
                + "============================================================================================================");
    }

    public static DataType DISCORD_USER;
    public static DataType DISCORD_GUILD;
    public static DataType MESSAGE_CHANNEL;
    public static DataType OFFSET_DATE_TIME;
    public static DataType CHANNEL_TYPE;

}
