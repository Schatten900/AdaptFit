import React, { useState, useRef,useEffect } from 'react';
import { View, Text, TouchableOpacity, ScrollView, Dimensions, Alert } from 'react-native';
import Slider from '@react-native-community/slider';
import { Ionicons } from '@expo/vector-icons';
import { useRouter } from 'expo-router';
import { MotiView } from 'moti';
import { useSetProfile } from '~/features/profile/hooks/useSetProfile';
import { authStorage } from '../../../commons/utils/authStorage';
import { ProfileFormData, GoalType, ExperienceLevel, Gender } from '~/types';
import {useMyProfile} from '../hooks/useMyProfile'

const { width } = Dimensions.get('window');

interface FormData {
  goal: string;
  experience: string;
  weight: number;
  height: number;
  age: number;
  gender: string;
}

export default function PersonalDataScreen() {
  const [currentIndex, setCurrentIndex] = useState(0);
  const scrollRef = useRef<ScrollView>(null);
  const router = useRouter();

  const redirectedRef = useRef(false);

  const {
    data: profile,
    isLoading,
    error: profileError,
  } = useMyProfile(true);


    const {
    setProfile,
    loading,
  } = useSetProfile();

  const [data, setData] = useState<FormData>({
    goal: '',
    experience: '',
    weight: 70,
    height: 170,
    age: 25,
    gender: ''
  });


  useEffect(() => {
    if (profile && !redirectedRef.current) {
      redirectedRef.current = true;
      router.replace('/home');
    }
  }, [profile]);


  if (isLoading) {
  return (
    <View className="flex-1 items-center justify-center bg-background">
      <Text className="text-text-secondary">Checking profile...</Text>
    </View>
  );
}

  const mapGoalToEnum = (goal: string): GoalType => {
    const goalMap: Record<string, GoalType> = {
      'Loss fat': 'FAT_LOSS',
      'Gain muscle': 'MUSCLE_GAIN',
      'Resistence': 'ENDURANCE',
    };
    return goalMap[goal] || 'GENERAL_FITNESS';
  };

  const mapExperienceToEnum = (experience: string): ExperienceLevel => {
    const expMap: Record<string, ExperienceLevel> = {
      'Beginner': 'BEGINNER',
      'Intermediate': 'INTERMEDIATE',
      'Advanced': 'ADVANCED',
    };
    return expMap[experience] || 'BEGINNER';
  };

  const mapGenderToEnum = (gender: string): Gender => {
    const genderMap: Record<string, Gender> = {
      'Male': 'MASCULINE',
      'Female': 'FEMININE',
      'Other': 'OTHER',
    };
    return genderMap[gender] || 'OTHER';
  };

  const steps = [
    { id: 'goal', title: 'What is your goal?', type: 'options' as const, options: ['Loss fat', 'Gain muscle', 'Resistence'] },
    { id: 'experience', title: 'What is your train level?', type: 'options' as const, options: ['Beginner', 'Intermediate', 'Advanced'] },
    { id: 'gender', title: 'What is your gender?', type: 'options' as const, options: ['Female', 'Male', 'Other'] },
    { id: 'weight', title: 'What is your weight?', type: 'slider' as const, min: 40, max: 150, unit: 'kg' },
    { id: 'height', title: 'What is your height?', type: 'slider' as const, min: 140, max: 220, unit: 'cm' },
    { id: 'age', title: 'How old are you?', type: 'slider' as const, min: 15, max: 90, unit: 'years' },
  ];

  const nextStep = async () => {
    if (currentIndex < steps.length - 1) {
      scrollRef.current?.scrollTo({ x: (currentIndex + 1) * width, animated: true });
      setCurrentIndex(currentIndex + 1);
    } else {
      if (!data.goal || !data.experience || !data.gender) {
        Alert.alert('Error', 'Please fill in all fields');
        return;
      }

      const formData: ProfileFormData = {
        goal: mapGoalToEnum(data.goal),
        experience: mapExperienceToEnum(data.experience),
        gender: mapGenderToEnum(data.gender),
        weight: data.weight,
        height: data.height,
        age: data.age,
      };

      await setProfile(formData);
      router.replace('/home');
    }
  };

  const prevStep = () => {
    if (currentIndex > 0) {
      scrollRef.current?.scrollTo({ x: (currentIndex - 1) * width, animated: true });
      setCurrentIndex(currentIndex - 1);
    }
  };

  const renderStep = (item: typeof steps[0]) => (
    <View className="items-center justify-center px-6 flex-1">
      <MotiView
        from={{ opacity: 0, scale: 0.9, translateY: 20 }}
        animate={{ opacity: 1, scale: 1, translateY: 0 }}
        transition={{ type: 'timing', duration: 500 }}
        className="bg-surface p-8 rounded-card border-thin border-stroke items-center justify-center w-full"
      >
        <Text className="text-text-secondary text-sm uppercase tracking-widest mb-2 font-semibold">
          Step {currentIndex + 1} of {steps.length}
        </Text>
        <Text className="text-text-primary text-2xl font-bold text-center mb-8">{item.title}</Text>

        {item.type === 'options' ? (
          <View className="w-full gap-y-4">
            {item.options?.map((opt) => (
              <TouchableOpacity
                key={opt}
                onPress={() => setData({ ...data, [item.id]: opt })}
                className={`p-4 rounded-premium border ${data[item.id as keyof FormData] === opt ? 'border-primary bg-primary/10' : 'border-stroke bg-background'}`}
              >
                <Text className={`text-center font-bold ${data[item.id as keyof FormData] === opt ? 'text-primary' : 'text-text-secondary'}`}>
                  {opt}
                </Text>
              </TouchableOpacity>
            ))}
          </View>
        ) : (
          <View className="w-full items-center">
            <View className="flex-row items-baseline mb-8">
              <Text className="text-primary text-6xl font-bold">{data[item.id as keyof FormData]}</Text>
              <Text className="text-text-secondary ml-2 text-xl">{item.unit}</Text>
            </View>
            <Slider
              style={{ width: '100%', height: 40 }}
              minimumValue={item.min}
              maximumValue={item.max}
              step={1}
              value={Number(data[item.id as keyof FormData])}
              onValueChange={(val) => setData({ ...data, [item.id]: val })}
              minimumTrackTintColor="#00E0A4"
              maximumTrackTintColor="#1E293B"
              thumbTintColor="#00E0A4"
            />
          </View>
        )}

        <TouchableOpacity
          onPress={nextStep}
          disabled={loading}
          className="bg-primary mt-12 w-full p-4 rounded-premium items-center active:opacity-70"
        >
          <Text className="text-primary font-bold text-lg text-center p-4">
            {currentIndex === steps.length - 1 ? (loading ? 'SAVING...' : 'CONTINUE') : 'CONTINUE'}
          </Text>
        </TouchableOpacity>
      </MotiView>
    </View>
  );

  return (
    <View className="flex-1 bg-background pt-12">
      <View className="px-6 flex-row items-center justify-between h-14">
        {currentIndex > 0 ? (
          <TouchableOpacity
            onPress={prevStep}
            className="w-10 h-10 items-center justify-center rounded-full bg-surface border-thin border-stroke"
          >
            <Ionicons name="arrow-back" size={20} color="#00E0A4" />
          </TouchableOpacity>
        ) : (
          <View className="w-10" />
        )}

        <View className="flex-row gap-x-1">
          {steps.map((_, i) => (
            <MotiView
              key={i}
              animate={{
                width: i === currentIndex ? 24 : 8,
                backgroundColor: i === currentIndex ? '#00E0A4' : '#1E293B'
              }}
              className="h-1.5 rounded-full"
            />
          ))}
        </View>
        <View className="w-10" />
      </View>

      <View className="px-6 mt-4 mb-4">
        <Text className="text-text-primary text-3xl font-bold">Configuration</Text>
        <Text className="text-text-secondary">Let's calibrate your AI Agent</Text>
      </View>

      <ScrollView
        ref={scrollRef}
        horizontal
        pagingEnabled
        scrollEnabled={false}
        showsHorizontalScrollIndicator={false}
        style={{flex: 1}}
      >
        {steps.map((item) => (
          <View key={item.id} style={{width}}>
            {renderStep(item)}
          </View>
        ))}
      </ScrollView>

      <View className="mb-12" />
    </View>
  );
}