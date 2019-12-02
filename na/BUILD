filegroup(name="resource_files", srcs = glob([
  "**/*.png",
  "**/*.gif",
  "**/*.properties"
]))

java_library(
  name="na",
  deps=[
    "//dju:dju",
    "//dmp:dmp",
    "@maven//:javax_mail_mail",
    "@maven//:javax_activation_activation"
  ],
  resources=[":resource_files"],
  srcs=glob(["src/**/*.java"])
)

java_binary(
  name="NewsAgent",
  runtime_deps = [ ":na" ],
  main_class = "org.dubh.apps.newsagent.NewsAgent"
)