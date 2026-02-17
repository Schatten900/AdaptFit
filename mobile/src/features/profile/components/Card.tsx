import React from 'react';
import { View, Text, TouchableOpacity } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { ProfileCardProps } from '~/types/profile/types';

export function Card({ title, value, icon, onPress }: ProfileCardProps) {
    const Content = () => (
        <View className="bg-surface p-4 rounded-premium border-thin border-stroke">
            <View className="flex-row items-center mb-2">
                {icon && <Ionicons name={icon as any} size={18} color="#00E0A4" />}
                <Text className="text-text-secondary text-xs font-semibold ml-2 uppercase tracking-wider">
                    {title}
                </Text>
            </View>
            <Text className="text-text-primary text-xl font-bold">
                {value ?? '—'}
            </Text>
        </View>
    );

    if (onPress) {
        return (
            <TouchableOpacity onPress={onPress} activeOpacity={0.8}>
                <Content />
            </TouchableOpacity>
        );
    }

    return <Content />;
}

export default Card;