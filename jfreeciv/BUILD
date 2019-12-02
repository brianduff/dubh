filegroup(name="resource_files", srcs = glob([
  "**/*.png",
  "**/*.gif",
  "**/*.properties",
  "**/*.po",
]))

java_import(
  name = "sixlegs_png",
  jars = [ "lib/png.jar" ],
)

java_library(
  name="jfreeciv_lib",
  deps=[
    ":sixlegs_png",
  ],
  resources=[":resource_files"],
  srcs=glob(["src/**/*.java"])
)

java_binary(
  name="JFreeCiv",
  runtime_deps = [ ":jfreeciv_lib" ],
  main_class = "org.freeciv.client.Client"
)