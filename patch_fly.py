with open("app/src/main/java/com/flylab/domain/model/Fly.kt", "r") as f:
    text = f.read()

text = text.replace("val id: String", "override val id: String")
text = text.replace("val genotype: String", "override val genotype: String")

with open("app/src/main/java/com/flylab/domain/model/Fly.kt", "w") as f:
    f.write(text)
