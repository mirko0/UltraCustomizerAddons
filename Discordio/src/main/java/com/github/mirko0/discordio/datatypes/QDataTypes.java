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
    private static final String DISCORD_MEMBER_NAME = PREFIX + "MEMBER";
    private static final String DISCORD_MESSAGE_NAME = PREFIX + "MESSAGE";


    public static DataTypeSpecification DISCORD_USER;
    public static DataTypeSpecification DISCORD_GUILD;

    public static DataTypeSpecification MESSAGE_CHANNEL;

    public static DataTypeSpecification OFFSET_DATE_TIME;

    public static DataTypeSpecification CHANNEL_TYPE;

    public static DataTypeSpecification DISCORD_MEMBER;

    public static DataTypeSpecification DISCORD_MESSAGE;

    public static void registerDataTypes() {
        registerDataType(USER_DATATYPE_NAME, new UserSpecification());
        registerDataType(GUILD_DATATYPE_NAME, new GuildSpecification());
        registerDataType(MESSAGE_CHANNEL_DATATYPE_NAME, new MessageChannelSpecification());
        registerDataType(OFFSET_DATE_TIME_NAME, new OffsetDateTimeSpecification());
        registerDataType(CHANNEL_TYPE_NAME, new ChannelTypeSpecification());
        registerDataType(DISCORD_MEMBER_NAME, new MemberSpecification());
        registerDataType(DISCORD_MESSAGE_NAME, new MessageSpecification());

        DISCORD_USER = DataType.getCustomDataType(USER_DATATYPE_NAME);
        DISCORD_GUILD = DataType.getCustomDataType(GUILD_DATATYPE_NAME);
        MESSAGE_CHANNEL = DataType.getCustomDataType(MESSAGE_CHANNEL_DATATYPE_NAME);
        OFFSET_DATE_TIME = DataType.getCustomDataType(OFFSET_DATE_TIME_NAME);
        CHANNEL_TYPE = DataType.getCustomDataType(CHANNEL_TYPE_NAME);
        DISCORD_MEMBER = DataType.getCustomDataType(DISCORD_MEMBER_NAME);
        DISCORD_MESSAGE = DataType.getCustomDataType(DISCORD_MESSAGE_NAME);
        AddonMain.log("Custom data types are registered.");
    }

    private static void registerDataType(String name, DataTypeSpecification specification) {
        DataType.registerCustomDataType(name, specification);
//        AddonMain.log("Data Type Registered: \n" + name + " (" + specification.getClass().getName() + ")\n"
//                + "============================================================================================================");
    }


}
