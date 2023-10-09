package fr.codecake.chatgptchallenge.flow.message.dto.gpt.enums;

public enum GPTModel {
    GPT_4("gpt-4"),
    GPT_4_0613("gpt-4-0613"),
    GPT_4_32K("gpt-4-32k"),
    GPT_4_32K_0613("gpt-4-32k-0613"),
    GPT_3_5_TURBO("gpt-3.5-turbo"),
    GPT_3_5_TURBO_0613("gpt-3.5-turbo-0613"),
    GPT_3_5_TURBO_16K("gpt-3.5-turbo-16k"),
    GPT_3_5_TURBO_16K_0613("gpt-3.5-turbo-16k-0613");

    private final String name;

    GPTModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static GPTModel fromString(String name) {
        for (GPTModel value : GPTModel.values()) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        throw new IllegalArgumentException("No enum found with name: " + name);
    }
}
