#!/bin/bash

# Overwrite versions.properties at the beginning
: > version.properties
: > gross-plugin/version.properties

# Define the prefixes
prefixes=("v")

# Get the latest commit hash
hash=$(git rev-parse --short HEAD)

# Check if the working directory is dirty
git diff --quiet || dirty=true
dirty=${dirty:-false}

# Loop over the prefixes
for prefix in "${prefixes[@]}"; do

    # Get the latest git tag from current branch that matches the current prefix
    tag=$(git describe --tags --match "${prefix}[0-9]*" --abbrev=0)

    # Trim the prefix from the start of the tag name
    version="${tag:1}"

    # Split the version into major, minor, and patch parts
    IFS='.' read -r -a version_parts <<< "$version"
    major="${version_parts[0]}"
    minor="${version_parts[1]}"
    patch="${version_parts[2]}"

    # Get the number of commits since the tag
    commit_count=$(git rev-list --count "${tag}"..HEAD)

    # Create the debug version string and append -dirty if necessary
    debug_version="${version}-${commit_count}-g${hash}"
    debug_version_suffix="${commit_count}-g${hash}"
    [[ $dirty == true ]] && debug_version+="-dirty"
    [[ $dirty == true ]] && debug_version_suffix+="-dirty"

    # Convert prefix to uppercase for property names
    prefix_upper=$(echo "$prefix" | tr '[:lower:]' '[:upper:]')

    # Print the result
    printf "%s_VERSION=%s\n" "$prefix_upper" "$version"
    printf "%s_DEBUG_VERSION=%s\n" "$prefix_upper" "$debug_version"
    printf "%s_DEBUG_VERSION_SUFFIX=%s\n" "$prefix_upper" "$debug_version_suffix"

    # Write to versions.properties
    {
        printf "%s_VERSION=%s\n" "$prefix_upper" "$version"
        printf "%s_DEBUG_VERSION_SUFFIX=%s\n" "$prefix_upper" "$debug_version_suffix"
        printf "%s_DEBUG_VERSION=%s\n" "$prefix_upper" "$debug_version"
    } >> version.properties
done

# Print more result
printf "HASH=%s\n" "$hash"
printf "DIRTY=%s\n" "$dirty"

# Write the hash to versions.properties after the loop
printf "HASH=%s\n" "$hash" >> version.properties
printf "DIRTY=%s\n" "$dirty" >> version.properties

cp version.properties gross-plugin/version.properties
