FROM openjdk:11-jdk

# Just matched `app/build.gradle`
ENV ANDROID_COMPILE_SDK "30"
# Just matched `app/build.gradle`
ENV ANDROID_BUILD_TOOLS "30.0.3"
# Version from https://developer.android.com/studio/releases/sdk-tools
ENV ANDROID_SDK_TOOLS "6858069"

ENV ANDROID_SDK_ROOT "${PWD}/android-sdk"

# install OS packages
RUN apt-get --quiet update --yes
RUN apt-get --quiet install --yes wget tar unzip lib32stdc++6 lib32z1 build-essential ruby ruby-dev git

# We use this for xxd hex->binary
RUN apt-get --quiet install --yes vim-common

# install Android SDK
# Create a new directory at specified location
RUN install -d $ANDROID_SDK_ROOT
RUN wget --output-document=$ANDROID_SDK_ROOT/cmdline-tools.zip https://dl.google.com/android/repository/commandlinetools-linux-${ANDROID_SDK_TOOLS}_latest.zip
# move to the archive at $ANDROID_SDK_ROOT
RUN bash -xc "\
pushd $ANDROID_SDK_ROOT; \
unzip cmdline-tools.zip; \
popd; \
"
ENV PATH="${PATH}:${ANDROID_SDK_ROOT}/cmdline-tools/bin/"
RUN sdkmanager --sdk_root=${ANDROID_SDK_ROOT} --version
# use yes to accept all licenses
RUN yes | sdkmanager --sdk_root=${ANDROID_SDK_ROOT} --licenses || true
RUN sdkmanager --sdk_root=${ANDROID_SDK_ROOT} "platforms;android-${ANDROID_COMPILE_SDK}"
RUN sdkmanager --sdk_root=${ANDROID_SDK_ROOT} "platform-tools"
RUN sdkmanager --sdk_root=${ANDROID_SDK_ROOT} "ndk-bundle"
RUN sdkmanager --sdk_root=${ANDROID_SDK_ROOT} "build-tools;${ANDROID_BUILD_TOOLS}"

# install Fastlane
COPY Gemfile.lock .
COPY Gemfile .
RUN gem install bundle
RUN bundle install