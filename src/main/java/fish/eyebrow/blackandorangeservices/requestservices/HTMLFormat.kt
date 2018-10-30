package fish.eyebrow.blackandorangeservices.requestservices

object HTMLFormat {
    fun formatEnquiryEmail(
            fullName: String,
            guestCount: String,
            location: String,
            eventType: String,
            comments: String,
            budget: String,
            date: String
    ): String {
        val emailStringBuilder = StringBuilder()

        emailStringBuilder.append("Hello, $fullName <br/><br/>")
                .append("We have received your enquiry with these details. ")
                .append("We will get back to you as soon as possible!")
                .append("<div style=\"background-color: floralwhite; margin: 15px; padding: 5px 25px 10px;")
                .append("color: #7d4f45; box-shadow: 7px 7px #4E4E4E;\">")
                .append("<h1>${eventType.toUpperCase()}</h1>")
                .append("<h3>guest count: $guestCount <br/> location: $location <br/> date: $date</h3>")
                .append(if (comments.isNotBlank()) "<h2>Your comments: <br/> $comments </h2>" else "")
                .append("<h3>${if (budget.isNotBlank()) budget else "No"} assumed budget</h3></div><br/>")
                .append("Please let us know if any information is incorrect ")
                .append("or if there is anything else we can do. <br/><br/>")
                .append("Kind regards, <br/> Manuela")

        return emailStringBuilder.toString()
    }

    fun convertStringBreaksToHTMLBreaks(string: String): String {
        return string.replace("\n", "<br/>")
    }
}