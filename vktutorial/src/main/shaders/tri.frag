#version 400
#extension GL_ARB_separate_shader_objects : enable
#extension GL_ARB_shading_language_420pack : enable
layout (location = 0) out vec4 uFragColor;
void main() {
    uFragColor = vec4(0.91, 0.26, 0.21, 1.0);
}