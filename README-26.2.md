# Boosters — Paper 26.2 port

Ported from the Fac-1 (Paper 1.12.2) build to Paper 26.2 for the sharded network.

Build: `mvn package` (Java 25). Jar: `target/Boosters-26.2.jar`.

- Paper API 26.2.build.133-stable (provided).
- Other PrismForge plugins this one uses are referenced as system jars from
  `../pitforge-26.2-libs/<Plugin>-26.2.jar` (copy the 26.2 builds there). They are soft dependencies in plugin.yml.
- MySQL/Redis drivers come from Paper's `libraries:` in plugin.yml; nothing is shaded.
- No credentials in this repo: database settings come from the PitforgeSetup-rendered config.yml.

Not runtime-tested on a 26.2 server yet; compiled against the real 26.2 API.
