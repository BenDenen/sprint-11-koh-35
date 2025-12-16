package ru.practicum.sprint_11_koh_33

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.TypeAdapter
import com.google.gson.annotations.SerializedName
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonClassDiscriminator
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Serializable
data class NewsResponse(
    val result: String,
    val data: Data
)

@Serializable
data class Data(
    val title: String,
    val items: List<NewsItem>
)

//data class NewsItem(
//    val id: String,
//    val title: String,
//    val type: String,
//    val created: Date,
//    val specificPropertyForSport:String?,
//    @SerializedName("specific_property_for_science")
//    val specificPropertyForScience:String?
//
//)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("type")
sealed class NewsItem {

    abstract val id: String
    abstract val title: String
    abstract val type: String
    @Serializable(with = DateSerializer::class)
    abstract val created: Date

    @Serializable
    @SerialName("sport")
    data class Sport(
        override val id: String,
        override val title: String,
        override val type: String,
        @Serializable(with = DateSerializer::class)
        override val created: Date,
        val specificPropertyForSport: String
    ) : NewsItem()

    @Serializable
    @SerialName("science")
    data class Science(
        override val id: String,
        override val title: String,
        override val type: String,
        @Serializable(with = DateSerializer::class)
        override val created: Date,
        @SerialName("specific_property_for_science")
        val specificPropertyForScience: String?
    ) : NewsItem()

    @Serializable
    @SerialName("social")
    data class Default(
        override val id: String,
        override val title: String,
        override val type: String,
        @Serializable(with = DateSerializer::class)
        override val created: Date
    ):NewsItem()

}

class NewsItemAdapter() : JsonDeserializer<NewsItem> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): NewsItem {
        val typeString = json.asJsonObject.getAsJsonPrimitive("type").asString
        return when(typeString) {
            "sport" -> {
                context.deserialize(json, NewsItem.Sport::class.java)
            }
            "science" -> {
                context.deserialize(json, NewsItem.Science::class.java)
            }
            else -> context.deserialize(json, NewsItem.Default::class.java)
        }
    }

}

class CustomDateTypeAdapter : TypeAdapter<Date>() {

    // https://ru.wikipedia.org/wiki/ISO_8601
    companion object {

        const val FORMAT_PATTERN = "yyyy-MM-DD'T'hh:mm:ss:SSS"

    }

    private val formatter = SimpleDateFormat(FORMAT_PATTERN, Locale.getDefault())
    override fun write(out: JsonWriter, value: Date) {
        out.value(formatter.format(value))
    }

    override fun read(`in`: JsonReader): Date {
        return formatter.parse(`in`.nextString())
    }

}

object DateSerializer : KSerializer<Date> {

    private val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override val descriptor = PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeString(df.format(value))
    }

    override fun deserialize(decoder: Decoder): Date {
        return df.parse(decoder.decodeString()) ?: Date()
    }

}
